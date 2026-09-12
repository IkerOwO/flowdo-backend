package com.iker.flowdo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.iker.flowdo.dtos.auth.LoginRequest;
import com.iker.flowdo.dtos.auth.LoginResponse;
import com.iker.flowdo.dtos.auth.RegisterUserRequest;
import com.iker.flowdo.entities.User;
import com.iker.flowdo.exceptions.auth.PasswordsDontMatchException;
import com.iker.flowdo.exceptions.auth.UserAlreadyExistsException;
import com.iker.flowdo.exceptions.auth.UserDontExistsException;
import com.iker.flowdo.repositories.AuthRepository;
import com.iker.flowdo.security.JwtService;
import com.iker.flowdo.security.SecurityUser;

@Service 
public class AuthService implements UserDetailsService {
    @Autowired 
    private AuthRepository repository;

    @Autowired 
    private JwtService jwtService;

    @Autowired 
    private PasswordEncoder encoder;

    public AuthService(AuthRepository repository, JwtService jwtService, PasswordEncoder encoder) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
            .map(SecurityUser::new)
            .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    public void registerUser(RegisterUserRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email associated with another account!");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setUsername(request.getUsername());
        user.setRole("ROLE_USER");
        repository.save(user);
    }

    public LoginResponse loginUser(LoginRequest request) {
        User user = repository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UserDontExistsException("The user doesn't exists!"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new PasswordsDontMatchException("Incorrect login!");
        }

        return new LoginResponse(
            jwtService.generateToken(user),
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole()
        );
    }

    
}
