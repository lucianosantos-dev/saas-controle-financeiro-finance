package com.lucianodev.saascontrolefinanceirofinance.service;

import com.lucianodev.saascontrolefinanceirofinance.dto.request.*;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.LoginResponse;
import com.lucianodev.saascontrolefinanceirofinance.entity.TokenVerificacao;
import com.lucianodev.saascontrolefinanceirofinance.entity.Usuario;
import com.lucianodev.saascontrolefinanceirofinance.enums.TipoVerificacao;
import com.lucianodev.saascontrolefinanceirofinance.exception.EmailNaoVerificadoException;
import com.lucianodev.saascontrolefinanceirofinance.exception.UsuarioInativoException;
import com.lucianodev.saascontrolefinanceirofinance.exception.UsuarioNaoEncontradoException;
import com.lucianodev.saascontrolefinanceirofinance.factory.AuthFactory;
import com.lucianodev.saascontrolefinanceirofinance.factory.TokenFactory;
import com.lucianodev.saascontrolefinanceirofinance.factory.UsuarioFactory;
import com.lucianodev.saascontrolefinanceirofinance.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private TokenVerificacaoService tokenService;
    @Mock
    private JwtEncoder jwtEncoder;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailService emailService;

    private Usuario usuario;
    private UsuarioRequest usuarioRequest;
    private LoginRequest loginRequest;
    private EmailRequest emailRequest;
    private AlterarSenhaRequest request;


    @BeforeEach
    public void setUp() {
        usuario = UsuarioFactory.usuarioValido();
        usuarioRequest = UsuarioFactory.usuarioRequestValido();
        loginRequest = AuthFactory.loginRequestValido();
        emailRequest = AuthFactory.emailRequestValido();
        request = AuthFactory.alterarSenhaRequestValido();
    }


    @Test
    public void deveFazerLogin_QuandoAsCredenciaisForemValidas() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(loginRequest.getSenha(), usuario.getSenhaHash())).thenReturn(true);

        Jwt jwtFake = mock(Jwt.class);
        when(jwtFake.getTokenValue()).thenReturn("kffrrir4594e33o303wwow23022-2222033iednddnd");
        when(jwtEncoder.encode(any())).thenReturn(jwtFake);


        LoginResponse response = authService.login(loginRequest);
        assertEquals("kffrrir4594e33o303wwow23022-2222033iednddnd", response.getTokenAcesso());
        assertNotNull(response);


        verify(usuarioRepository, times(1)).findByEmail(usuario.getEmail());
        verify(passwordEncoder, times(1)).matches(loginRequest.getSenha(), usuario.getSenhaHash());
    }

    @Test
    public void deveLancarBadCredentialsException_QuandoTentarBuscarUsuarioQueNaoExiste() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));

        verify(jwtEncoder, never()).encode(any());
    }

    @Test
    public void deveLancarBadCredentialsException_QuandoSenhaForIncorreta() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(loginRequest.getSenha(), usuario.getSenhaHash())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));

        verify(usuarioRepository, times(1)).findByEmail(usuario.getEmail());
        verify(jwtEncoder, never()).encode(any());
    }

    @Test
    public void deveLancarEmailNaoVerificadoException_QuandoEmailNaoForVerificado() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(loginRequest.getSenha(), usuario.getSenhaHash())).thenReturn(true);
        usuario.setEmailVerificado(false);


        assertThrows(EmailNaoVerificadoException.class, () -> authService.login(loginRequest));

        verify(usuarioRepository, times(1)).findByEmail(usuario.getEmail());
        verify(passwordEncoder, times(1)).matches(loginRequest.getSenha(), usuario.getSenhaHash());
        verify(jwtEncoder, never()).encode(any());
    }

    @Test
    public void deveLancarUsuarioInativoException_QuandoUsuarioNaoForValido() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(loginRequest.getSenha(), usuario.getSenhaHash())).thenReturn(true);
        usuario.setAtivo(false);

        assertThrows(UsuarioInativoException.class, () -> authService.login(loginRequest));

        verify(usuarioRepository, times(1)).findByEmail(usuario.getEmail());
        verify(passwordEncoder, times(1)).matches(loginRequest.getSenha(), usuario.getSenhaHash());
        verify(jwtEncoder, never()).encode(any());
    }

    @Test
    public void deveConfirmarEmailComSucesso() {
        TokenVerificacao tokenFalso = TokenFactory.tokenVerificacaoValido();
        String token = tokenFalso.getToken();

        when(tokenService.validarToken(token)).thenReturn(tokenFalso);
        authService.confirmarEmail(token);

        verify(tokenService, times(1)).consumirToken(tokenFalso);
        verify(usuarioService, times(1)).marcarEmailComoVerificado(tokenFalso.getUsuario());
    }

    @Test
    public void deveRedefinirSenhaComSucesso() {
        NovaSenhaRequest request = AuthFactory.novaSenhaRequest();
        TokenVerificacao tokenFake = TokenFactory.tokenVerificacaoValido();

        when(tokenService.validarToken(request.token())).thenReturn(tokenFake);
        when(passwordEncoder.encode(request.novaSenha())).thenReturn("senha_hash_fake");

        authService.redefinirSenha(request);

        verify(usuarioRepository, times(1)).save(tokenFake.getUsuario());
        verify(tokenService, times(1)).consumirToken(tokenFake);
    }

    @Test
    public void deveSolicitarRedefinicao_QuandoEmailExistir() {
        when(usuarioRepository.findByEmail(emailRequest.email())).thenReturn(Optional.of(usuario));
        when(tokenService.gerarToken(usuario, TipoVerificacao.REDEFINICAO_SENHA)).thenReturn("string_qualquer_aleatoria");

        authService.solicitarRedefinicao(emailRequest);

        verify(usuarioRepository, times(1)).findByEmail(emailRequest.email());
        verify(tokenService, times(1)).gerarToken(usuario, TipoVerificacao.REDEFINICAO_SENHA);
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    public void naoDeveFazerNada_QuandoEmailNaoExistir() {
        when(usuarioRepository.findByEmail(emailRequest.email())).thenReturn(Optional.empty());

        authService.solicitarRedefinicao(emailRequest);

        verify(tokenService, never()).gerarToken(any(), any());
        verify(emailService, never()).sendEmail(any(), any(), any());
    }

    @Test
    public void deveAlterarSenhaComSucesso() {
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(request.senhaAtual(), usuario.getSenhaHash())).thenReturn(true);
        when(passwordEncoder.encode(request.novaSenha())).thenReturn("SENHA_HASH_BOLADONA");

        String hashAntigo = usuario.getSenhaHash();
        authService.alterarSenha(usuario.getId(), request);

        verify(usuarioRepository, times(1)).findById(usuario.getId());
        verify(passwordEncoder, times(1)).matches(request.senhaAtual(), hashAntigo);
        verify(passwordEncoder, times(1)).encode(request.novaSenha());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    public void deveLancarUsuarioNaoEncontradoException_QuandoAlterarSenhaComIdInexistente() {
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () -> authService.alterarSenha(usuario.getId(), request));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    public void deveLancarBadCredentialsException_QuandoSenhaAtualEstiverIncorreta() {
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(request.senhaAtual(), usuario.getSenhaHash())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authService.alterarSenha(usuario.getId(), request));

        verify(usuarioRepository, never()).save(any());
    }
}
