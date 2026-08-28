package com.lucianodev.saascontrolefinanceirofinance.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super("Recurso com Id: " + message + " fornecido não encontrado.");
    }
}
