package com.lucianodev.saascontrolefinanceirofinance.exception;

public class TokenInvalidoException extends RuntimeException {
    public TokenInvalidoException(String message) {
        super("Erro! Token inválido: " + message);
    }
}
