package com.example.Zecury.ExceptionsforThisAppliccation;


public class InsufficiantAmountException extends RuntimeException{


    public InsufficiantAmountException(String message) {
        super(message);
    }
}
