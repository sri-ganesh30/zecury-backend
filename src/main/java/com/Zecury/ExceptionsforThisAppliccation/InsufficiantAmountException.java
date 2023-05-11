package com.Zecury.ExceptionsforThisAppliccation;


public class InsufficiantAmountException extends RuntimeException{


    public InsufficiantAmountException(String message) {
        super(message);
    }
}
