package com.lucianodev.saascontrolefinanceirofinance.service;

import com.lucianodev.saascontrolefinanceirofinance.dto.request.LoginRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.LoginResponse;
import com.lucianodev.saascontrolefinanceirofinance.entity.TokenVerificacao;
import com.lucianodev.saascontrolefinanceirofinance.entity.Usuario;
import com.lucianodev.saascontrolefinanceirofinance.exception.EmailNaoVerificadoException;
import com.lucianodev.saascontrolefinanceirofinance.repository.UsuarioRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class AuthService {

    private final UsuarioService usuarioService;
    private final TokenVerificacaoService tokenVerificacaoService;
    private final UsuarioRepository usuarioRepository;
    private final JwtEncoder jwtEncoder;
    private final PasswordEncoder passwordEncoder;


    public AuthService(UsuarioService usuarioService, TokenVerificacaoService tokenVerificacaoService, UsuarioRepository usuarioRepository, JwtEncoder jwtEncoder, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.tokenVerificacaoService = tokenVerificacaoService;
        this.usuarioRepository = usuarioRepository;
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void confirmarEmail(String token) {
        TokenVerificacao tokenValido = tokenVerificacaoService.validarToken(token);
        tokenVerificacaoService.consumirToken(tokenValido);
        usuarioService.marcarEmailComoVerificado(tokenValido.getUsuario());
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Optional<Usuario> optUser = usuarioRepository.findByEmail(request.getEmail());

        if (optUser.isEmpty() || !isPasswordCorrect(request.getSenha(), optUser.get().getSenhaHash())) {
            throw new BadCredentialsException("Usuário ou senha incorretos!");
        }
        if (!optUser.get().getEmailVerificado()) {
            throw new EmailNaoVerificadoException();
        }

        Usuario usuarioSalvo = optUser.get();

        long expiracaoEm = 600L;

        JwtClaimsSet jwt = JwtClaimsSet.builder()
                .issuer("saas-financeiro")
                .subject(usuarioSalvo.getId().toString())
                .expiresAt(Instant.now().plusSeconds(expiracaoEm))
                .issuedAt(Instant.now())
                .claim("email", usuarioSalvo.getEmail())
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(jwt)).getTokenValue();

        return LoginResponse.builder()
                .tokenAcesso(token)
                .expiraEm(expiracaoEm)
                .build();
    }

    private boolean isPasswordCorrect(String senha, String senhaSalva) {
        return passwordEncoder.matches(senha, senhaSalva);
    }
}
