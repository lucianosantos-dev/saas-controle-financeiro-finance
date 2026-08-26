package com.lucianodev.saascontrolefinanceirofinance.service;

import com.lucianodev.saascontrolefinanceirofinance.entity.TokenVerificacao;
import com.lucianodev.saascontrolefinanceirofinance.entity.Usuario;
import com.lucianodev.saascontrolefinanceirofinance.enums.TipoVerificacao;
import com.lucianodev.saascontrolefinanceirofinance.exception.TokenExpiradoException;
import com.lucianodev.saascontrolefinanceirofinance.exception.TokenInvalidoException;
import com.lucianodev.saascontrolefinanceirofinance.exception.TokenUsadoException;
import com.lucianodev.saascontrolefinanceirofinance.factory.TokenFactory;
import com.lucianodev.saascontrolefinanceirofinance.factory.UsuarioFactory;
import com.lucianodev.saascontrolefinanceirofinance.repository.TokenVerificacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenVerificacaoServiceTest {

    @InjectMocks
    private TokenVerificacaoService tokenService;
    @Mock
    private TokenVerificacaoRepository tokenRepository;

    private TipoVerificacao tipoVerificacao;
    private Usuario usuario;
    private TokenVerificacao tokenVerificacao;
    private TokenVerificacao tokenExpirado;
    private TokenVerificacao tokenUsado;

    @BeforeEach
    public void setUp() {
        usuario = UsuarioFactory.usuarioValido();
        tokenVerificacao = TokenFactory.tokenVerificacaoValido();
        tipoVerificacao = TipoVerificacao.CONFIRMACAO_EMAIL;
        tokenExpirado = TokenFactory.tokenVerificacaoExpirado();
        tokenUsado = TokenFactory.tokenVerificacaoUsado();
    }

    @Test
    public void deveGerarTokenComSucesso() {
        when(tokenRepository.save(any(TokenVerificacao.class))).thenReturn(tokenVerificacao);

        String tokenGerado = tokenService.gerarToken(usuario, tipoVerificacao);

        ArgumentCaptor<TokenVerificacao> captor = ArgumentCaptor.forClass(TokenVerificacao.class);

        verify(tokenRepository, times(1)).save(captor.capture());

        TokenVerificacao tokenSalvo = captor.getValue();

        assertNotNull(tokenGerado);
        assertEquals(usuario, tokenSalvo.getUsuario());
        assertTrue(tokenSalvo.getExpiraEm().isAfter(LocalDateTime.now()));
        assertEquals(tipoVerificacao, tokenSalvo.getTipoVerificacao());
    }

    @Test
    public void deveLancarTokenInvalidoException_QuandoTentarBuscarToken() {
        when(tokenRepository.findByToken(tokenVerificacao.getToken())).thenReturn(Optional.empty());

        assertThrows(TokenInvalidoException.class, () -> tokenService.validarToken(tokenVerificacao.getToken()));

        verify(tokenRepository, times(1)).findByToken(any());
    }

    @Test
    public void deveLancarExcecao_QuandoTokenEstiverExpirado() {
        when(tokenRepository.findByToken(tokenExpirado.getToken())).thenReturn(Optional.of(tokenExpirado));

        assertThrows(TokenExpiradoException.class, () -> tokenService.validarToken(tokenExpirado.getToken()));

        verify(tokenRepository, times(1)).findByToken(tokenExpirado.getToken());
    }

    @Test
    public void deveLancarTokenUsadoException_QuandoTokenForUsado() {
        when(tokenRepository.findByToken(tokenUsado.getToken())).thenReturn(Optional.of(tokenUsado));

        assertThrows(TokenUsadoException.class, () -> tokenService.validarToken(tokenUsado.getToken()));

        verify(tokenRepository, times(1)).findByToken(tokenUsado.getToken());
    }

    @Test
    public void deveConsumirTokenComSucesso() {
        tokenService.consumirToken(tokenVerificacao);

        assertTrue(tokenVerificacao.foiUsado());

        verify(tokenRepository, times(1)).save(tokenVerificacao);
    }
}