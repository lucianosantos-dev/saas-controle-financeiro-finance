package com.lucianodev.saascontrolefinanceirofinance.service;

import com.lucianodev.saascontrolefinanceirofinance.entity.TokenVerificacao;
import com.lucianodev.saascontrolefinanceirofinance.entity.Usuario;
import com.lucianodev.saascontrolefinanceirofinance.enums.TipoVerificacao;
import com.lucianodev.saascontrolefinanceirofinance.exception.TokenExpiradoException;
import com.lucianodev.saascontrolefinanceirofinance.exception.TokenInvalidoException;
import com.lucianodev.saascontrolefinanceirofinance.exception.TokenUsadoException;
import com.lucianodev.saascontrolefinanceirofinance.repository.TokenVerificacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenVerificacaoService {

    private final TokenVerificacaoRepository repository;

    public TokenVerificacaoService(TokenVerificacaoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public String gerarToken(Usuario usuario, TipoVerificacao tipo) {
        String tokenGerado = UUID.randomUUID().toString();

        TokenVerificacao token = new TokenVerificacao();

        token.setTipoVerificacao(tipo);
        token.setToken(tokenGerado);
        token.setExpiraEm(LocalDateTime.now().plusDays(1));
        token.setUsuario(usuario);

        repository.save(token);
        return tokenGerado;
    }

    public TokenVerificacao validarToken(String token) {
        TokenVerificacao encontrado = repository.findByToken(token)
                .orElseThrow(() -> new TokenInvalidoException(token));

        if (encontrado.estaExpirado()) {
            throw new TokenExpiradoException(token);
        }
        if (encontrado.foiUsado()) {
            throw new TokenUsadoException(token);
        }
        return encontrado;
    }

    public void consumirToken(TokenVerificacao token){
        token.marcarComoUsado();
        repository.save(token);
    }

}
