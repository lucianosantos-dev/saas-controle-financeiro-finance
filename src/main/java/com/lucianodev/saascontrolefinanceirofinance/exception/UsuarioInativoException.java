package com.lucianodev.saascontrolefinanceirofinance.exception;

public class UsuarioInativoException extends RuntimeException {
    public UsuarioInativoException() {
        super("Usuário não pode fazer login pois está inativo.");
    }
}
