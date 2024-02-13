package com.olim.reserveservice.exception.customexception;


public class AuthenticationFailException extends RuntimeException {
    public AuthenticationFailException(String message) {
        super(message);
    }
}
