package com.lucianodev.saascontrolefinanceirofinance.exception;

public class TokenExpiradoException extends RuntimeException {
    public TokenExpiradoException(String message) {
        super("Erro! Token fornecido está expirado: " + message);
    }
}
