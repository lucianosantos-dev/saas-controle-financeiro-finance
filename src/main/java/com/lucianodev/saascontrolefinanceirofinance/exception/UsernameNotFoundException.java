package com.lucianodev.saascontrolefinanceirofinance.exception;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException() {
      super("Usuário não encontrado com e-mail fornecido.");
    }
}
