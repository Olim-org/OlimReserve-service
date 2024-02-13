package com.olim.reserveservice.exception.customexception;

public class PermissionFailException extends RuntimeException{
    public PermissionFailException(String message) {
        super(message);
    }
}
