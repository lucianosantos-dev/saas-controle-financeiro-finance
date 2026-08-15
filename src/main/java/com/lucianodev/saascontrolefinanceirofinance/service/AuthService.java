package com.lucianodev.saascontrolefinanceirofinance.service;

import com.lucianodev.saascontrolefinanceirofinance.entity.TokenVerificacao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioService usuarioService;
    private final TokenVerificacaoService tokenVerificacaoService;

    public AuthService(UsuarioService usuarioService, TokenVerificacaoService tokenVerificacaoService) {
        this.usuarioService = usuarioService;
        this.tokenVerificacaoService = tokenVerificacaoService;
    }

    @Transactional
    public void confirmarEmail(String token){
        TokenVerificacao tokenValido = tokenVerificacaoService.validarToken(token);
        tokenVerificacaoService.consumirToken(tokenValido);
        usuarioService.marcarEmailComoVerificado(tokenValido.getUsuario());
    }
}
