package com.lucianodev.saascontrolefinanceirofinance.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException() {
      super("Usuário não encontrado com e-mail fornecido.");
    }
}
