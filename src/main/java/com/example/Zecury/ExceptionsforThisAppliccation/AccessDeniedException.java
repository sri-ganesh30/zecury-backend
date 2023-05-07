package com.example.Zecury.ExceptionsforThisAppliccation;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String message) {
        super(message);
    }
}
