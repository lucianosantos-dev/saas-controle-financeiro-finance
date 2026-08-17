package com.lucianodev.saascontrolefinanceirofinance.exception;

public class EmailNaoVerificadoException extends RuntimeException {
    public EmailNaoVerificadoException() {
        super("Por favor! Verifique seu  e-mail antes de efetuar login.");
    }
}
