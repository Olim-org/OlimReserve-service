package com.olim.reserveservice.exception.customexception;

public class VerifyEmailFailException extends RuntimeException {
    public VerifyEmailFailException(String message) {
        super(message);
    }
}
