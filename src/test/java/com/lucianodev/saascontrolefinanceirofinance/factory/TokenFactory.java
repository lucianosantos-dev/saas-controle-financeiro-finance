package com.lucianodev.saascontrolefinanceirofinance.factory;

import com.lucianodev.saascontrolefinanceirofinance.entity.TokenVerificacao;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.lucianodev.saascontrolefinanceirofinance.factory.UsuarioFactory.usuarioValido;

public class TokenFactory {

    public static TokenVerificacao tokenVerificacaoValido() {
        TokenVerificacao tokenVerificacao = new TokenVerificacao();

        tokenVerificacao.setId(UUID.randomUUID());
        tokenVerificacao.setToken(UUID.randomUUID().toString());
        tokenVerificacao.setExpiraEm(LocalDateTime.now().plusDays(1));
        tokenVerificacao.setUsuario(usuarioValido());
        return tokenVerificacao;
    }
}
