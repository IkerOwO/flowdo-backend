package com.iker.flowdo.dtos.auth;

public record LoginResponse( 
    String token,
    Long id,
    String username,
    String email,
    String role
) {}
