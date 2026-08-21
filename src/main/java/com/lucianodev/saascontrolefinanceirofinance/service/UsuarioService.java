package com.lucianodev.saascontrolefinanceirofinance.service;

import com.lucianodev.saascontrolefinanceirofinance.dto.request.UsuarioRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.request.UsuarioUpdateRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.UsuarioResponse;
import com.lucianodev.saascontrolefinanceirofinance.entity.Role;
import com.lucianodev.saascontrolefinanceirofinance.entity.Usuario;
import com.lucianodev.saascontrolefinanceirofinance.enums.TipoVerificacao;
import com.lucianodev.saascontrolefinanceirofinance.exception.*;
import com.lucianodev.saascontrolefinanceirofinance.mapper.UsuarioMapper;
import com.lucianodev.saascontrolefinanceirofinance.repository.RoleRepository;
import com.lucianodev.saascontrolefinanceirofinance.repository.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TokenVerificacaoService tokenVerificacaoService;
    private final RoleRepository roleRepository;

    public UsuarioService(UsuarioRepository repository,
                          UsuarioMapper usuarioMapper,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService,
                          TokenVerificacaoService tokenVerificacaoService,
                          RoleRepository roleRepository) {

        this.repository = repository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.tokenVerificacaoService = tokenVerificacaoService;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public UsuarioResponse create(UsuarioRequest request) {
        validaEmail(request.email());
        Role userRole = roleRepository.findByNome("USER")
                .orElseThrow(() -> new RoleNaoEncontradaException("Role não encontrada"));
        Usuario usuario = usuarioMapper.toEntity(request);

        usuario.getRoles().add(userRole);
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        try {
            Usuario salvo = repository.save(usuario);

            String tokeGerado = tokenVerificacaoService.gerarToken(salvo, TipoVerificacao.CONFIRMACAO_EMAIL);

            enviarEmailConfirmacao(salvo, tokeGerado);

            return usuarioMapper.toResponse(salvo);

        } catch (DataIntegrityViolationException e) {
            throw new ConflictException();
        }
    }

    @Transactional
    public UsuarioResponse update(UUID id, UsuarioUpdateRequest request) {
        Usuario user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id.toString()));

        usuarioMapper.atualizarUsuario(request, user);

        Usuario atualizado = repository.save(user);
        return usuarioMapper.toResponse(atualizado);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse findByEmail(String email) {
        Usuario user = repository.findByEmail(email)
                .orElseThrow(UsuarioNaoEncontradoException::new);
        return usuarioMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse findById(UUID id) {
        Usuario user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id.toString()));
        return usuarioMapper.toResponse(user);
    }


    @Transactional
    public void desativarUsuario(UUID idUsuario) {
        Usuario user = repository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException(idUsuario.toString()));

        user.desativar();
        repository.save(user);
    }

    private void enviarEmailConfirmacao(Usuario usuario, String token) {
        String destino = usuario.getEmail();
        String assunto = "FINANCE Controle Financeiro: Confirmação de e-mail";
        String link = "http://localhost:8080/auth/confirmar-email/?token=" + token;

        String corpo = "Olá " + usuario.getNome() + "!\n\n"
                + "Falta pouco para você acessar o sistema.\n"
                + "Clique no link abaixo para verificar seu e-mail:\n"
                + link;

        emailService.sendEmail(destino, assunto, corpo);
    }

    @Transactional
    public void marcarEmailComoVerificado(Usuario usuario) {
        usuario.setEmailVerificado(true);
        repository.save(usuario);
    }

    private void validaEmail(String email) {
        if (repository.existsByEmail(email)) {
            throw new ConflictException();
        }
    }
}
