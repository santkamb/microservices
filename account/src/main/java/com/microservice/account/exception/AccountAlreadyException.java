package com.microservice.account.exception;

public class AccountAlreadyException extends Exception {
    public AccountAlreadyException(String message) {
        super(message);
    }
}
