package com.iker.flowdo.security;

import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;
import com.iker.flowdo.entities.User;
import io.jsonwebtoken.Jwts;

@Service 
public class JwtService {
    private final SecretKey signingKey = Jwts.SIG.HS256.key().build();
    private final long expiration = 86400000;

    public String generateToken(User user) {
        Date now = new Date();

        return Jwts.builder()
            .subject(user.getUsername())
            .claim("role", user.getRole())
            .issuedAt(now)
            .expiration(new Date(now.getTime() + expiration))
            .signWith(signingKey)
            .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            extractUsername(token);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
