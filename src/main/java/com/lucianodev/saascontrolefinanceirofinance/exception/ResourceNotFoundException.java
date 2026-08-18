package com.lucianodev.saascontrolefinanceirofinance.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super("Usuário com Id: " + message + " fornecido não encontrado.");
    }
}
