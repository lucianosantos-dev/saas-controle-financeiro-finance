package com.lucianodev.saascontrolefinanceirofinance.service;

import com.lucianodev.saascontrolefinanceirofinance.dto.request.EmailRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.request.LoginRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.request.NovaSenhaRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.LoginResponse;
import com.lucianodev.saascontrolefinanceirofinance.entity.TokenVerificacao;
import com.lucianodev.saascontrolefinanceirofinance.entity.Usuario;
import com.lucianodev.saascontrolefinanceirofinance.enums.TipoVerificacao;
import com.lucianodev.saascontrolefinanceirofinance.exception.EmailNaoVerificadoException;
import com.lucianodev.saascontrolefinanceirofinance.exception.UsuarioInativoException;
import com.lucianodev.saascontrolefinanceirofinance.repository.UsuarioRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final UsuarioService usuarioService;
    private final TokenVerificacaoService tokenVerificacaoService;
    private final UsuarioRepository usuarioRepository;
    private final JwtEncoder jwtEncoder;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    public AuthService(UsuarioService usuarioService,
                       TokenVerificacaoService tokenVerificacaoService,
                       UsuarioRepository usuarioRepository,
                       JwtEncoder jwtEncoder,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService
                       ) {

        this.usuarioService = usuarioService;
        this.tokenVerificacaoService = tokenVerificacaoService;
        this.usuarioRepository = usuarioRepository;
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public void confirmarEmail(String token) {
        TokenVerificacao tokenValido = tokenVerificacaoService.validarToken(token);
        tokenVerificacaoService.consumirToken(tokenValido);
        usuarioService.marcarEmailComoVerificado(tokenValido.getUsuario());
    }

    @Transactional
    public void redefinirSenha(NovaSenhaRequest request) {

        TokenVerificacao tokenValido = tokenVerificacaoService.validarToken(request.token());

        Usuario usuario = tokenValido.getUsuario();

        usuario.setSenhaHash(passwordEncoder.encode(request.novaSenha()));

        usuarioRepository.save(usuario);

        tokenVerificacaoService.consumirToken(tokenValido);
    }

    @Transactional
    public void solicitarRedefinicao(EmailRequest request) {
        Usuario user = usuarioRepository.findByEmail(request.email())
                .orElse(null);

        if (user == null) return;

        String token = tokenVerificacaoService.gerarToken(user, TipoVerificacao.REDEFINICAO_SENHA);

        enviarEmailRedefinicaoSenha(user, token);
    }

    private void enviarEmailRedefinicaoSenha(Usuario usuario, String token) {
        String destino = usuario.getEmail();
        String assunto = "FINANCE Controle Financeiro: Redefinição de Senha";
        String link = "http://localhost:4200/auth/redefinir-senha/?token=" + token;

        String corpo = "Olá " + usuario.getNome() + "!\n\n"
                + "Você solicitou a redefinição da sua senha.\n"
                + "Clique no link abaixo para criar uma nova senha:\n"
                + link;

        emailService.sendEmail(destino, assunto, corpo);
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
        if (!optUser.get().getAtivo()) {
            throw new UsuarioInativoException();
        }

        Usuario usuarioSalvo = optUser.get();

        long expiracaoEm = 600L;

        List<String> roles = usuarioSalvo.getRoles().stream()
                .map(role -> "ROLE_" + role.getNome())
                .toList();

        JwtClaimsSet jwt = JwtClaimsSet.builder()
                .issuer("saas-financeiro")
                .subject(usuarioSalvo.getId().toString())
                .expiresAt(Instant.now().plusSeconds(expiracaoEm))
                .issuedAt(Instant.now())
                .claim("scope", String.join(" ", roles))
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
