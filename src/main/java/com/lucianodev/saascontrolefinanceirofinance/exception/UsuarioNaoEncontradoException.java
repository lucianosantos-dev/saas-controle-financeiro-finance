package com.lucianodev.saascontrolefinanceirofinance.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException() {
      super("Erro ao tentar encontrar usuário");
    }
}
