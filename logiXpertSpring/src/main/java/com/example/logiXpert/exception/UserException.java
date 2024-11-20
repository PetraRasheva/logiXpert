package com.example.logiXpert.exception;

import org.springframework.http.HttpStatus;

public class UserException extends RuntimeException {
    public UserException(String message, HttpStatus notFound) {
        super(message);
    }
}
