package com.example.logiXpert.exception;

public class ShipmentNotFoundException extends RuntimeException {
    public ShipmentNotFoundException(String message ) {
        super(message);
    }
}
