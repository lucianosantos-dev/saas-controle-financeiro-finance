package com.lucianodev.saascontrolefinanceirofinance.exception;

public class ConflictException extends RuntimeException {
    public ConflictException() {
        super("Erro ao cadastrar usuário! Este e-mail já está em uso.");
    }
}
