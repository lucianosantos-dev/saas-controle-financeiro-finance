package com.lucianodev.saascontrolefinanceirofinance.exception;

public class TokenUsadoException extends RuntimeException {
    public TokenUsadoException() {
        super("Erro! Token foi utilizado.");
    }
}
