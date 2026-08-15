package com.lucianodev.saascontrolefinanceirofinance.exception;

public class TokenUsadoException extends RuntimeException {
    public TokenUsadoException(String message) {
        super("Erro! Token foi utilizado: " + message);
    }
}
