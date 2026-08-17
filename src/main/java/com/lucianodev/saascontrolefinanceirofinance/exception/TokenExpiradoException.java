package com.lucianodev.saascontrolefinanceirofinance.exception;

public class TokenExpiradoException extends RuntimeException {
    public TokenExpiradoException() {
        super("Erro! Token fornecido está expirado.");
    }
}
