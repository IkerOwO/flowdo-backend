package com.iker.flowdo.exceptions.auth;

public class UserDontExistsException extends RuntimeException {
    public UserDontExistsException(String message) {
        super(message);
    }
}
