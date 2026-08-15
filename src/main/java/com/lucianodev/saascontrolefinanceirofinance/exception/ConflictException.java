package com.lucianodev.saascontrolefinanceirofinance.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super("Erro ao cadastrar! Usuário já foi cadastrado com e-mail: " + message);
    }
}
