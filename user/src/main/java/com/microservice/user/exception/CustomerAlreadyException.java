package com.microservice.user.exception;

public class CustomerAlreadyException extends Exception {
    public CustomerAlreadyException(String msg)
    {
        super(msg);
    }
}
