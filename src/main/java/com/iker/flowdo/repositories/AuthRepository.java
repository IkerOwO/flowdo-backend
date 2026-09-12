package com.iker.flowdo.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.iker.flowdo.entities.User;

public interface AuthRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
