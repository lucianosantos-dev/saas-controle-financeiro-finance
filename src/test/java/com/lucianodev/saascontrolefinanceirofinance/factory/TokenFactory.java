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

    public static TokenVerificacao tokenVerificacaoExpirado() {
        TokenVerificacao token = tokenVerificacaoValido();
        token.setExpiraEm(LocalDateTime.now().minusDays(1));
        return token;
    }

    public static TokenVerificacao tokenVerificacaoUsado(){
        TokenVerificacao token = tokenVerificacaoValido();

        token.setExpiraEm(LocalDateTime.now().plusDays(1));
        token.setUsadoEm(LocalDateTime.now());
        return token;
    }
}
