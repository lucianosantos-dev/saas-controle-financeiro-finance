package com.lucianodev.saascontrolefinanceirofinance.service;

import com.lucianodev.saascontrolefinanceirofinance.dto.request.UsuarioRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.request.UsuarioUpdateRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.UsuarioResponse;
import com.lucianodev.saascontrolefinanceirofinance.entity.Role;
import com.lucianodev.saascontrolefinanceirofinance.entity.Usuario;
import com.lucianodev.saascontrolefinanceirofinance.enums.TipoVerificacao;
import com.lucianodev.saascontrolefinanceirofinance.exception.*;
import com.lucianodev.saascontrolefinanceirofinance.factory.UsuarioFactory;
import com.lucianodev.saascontrolefinanceirofinance.mapper.UsuarioMapper;
import com.lucianodev.saascontrolefinanceirofinance.repository.RoleRepository;
import com.lucianodev.saascontrolefinanceirofinance.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;
    @Mock
    UsuarioRepository usuarioRepository;
    @Mock
    UsuarioMapper usuarioMapper;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    TokenVerificacaoService tokenVerificacaoService;
    @Mock
    RoleRepository roleRepository;
    @Mock
    EmailService emailService;

    private Usuario usuarioFake;
    private UsuarioResponse usuarioResponseFake;
    private UsuarioRequest usuarioRequestFake;
    private UsuarioUpdateRequest requestUpdateFake;
    private Role roleFake;


    @BeforeEach
    void setUp() {
        usuarioFake = UsuarioFactory.usuarioValido();
        usuarioRequestFake = UsuarioFactory.usuarioRequestValido();
        usuarioResponseFake = UsuarioFactory.usuarioResponseValido();
        requestUpdateFake = UsuarioFactory.usuarioUpdateRequestValido();
        roleFake = new Role();
    }

    @Test
    public void deveAtivarUsuario_QuandoUsuarioExistir() {
        usuarioFake.setAtivo(false);
        when(usuarioRepository.findById(usuarioFake.getId())).thenReturn(Optional.of(usuarioFake));

        usuarioService.ativarUsuario(usuarioFake.getId());

        assertTrue(usuarioFake.getAtivo());

        verify(usuarioRepository, times(1)).save(usuarioFake);
    }

    @Test
    public void deveLancarExcecao_QuandoTentarAtivarUsuarioInexistente() {
        when(usuarioRepository.findById(usuarioFake.getId())).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () ->
                usuarioService.ativarUsuario(usuarioFake.getId())
        );

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    public void deveDesativarUsuario_QuandoUsuarioExistir() {
        when(usuarioRepository.findById(usuarioFake.getId())).thenReturn(Optional.of(usuarioFake));

        usuarioService.desativarUsuario(usuarioFake.getId());

        assertFalse(usuarioFake.getAtivo());

        verify(usuarioRepository, times(1)).save(usuarioFake);
    }


    @Test
    public void deveLancarExcecao_QuandoTentarDesativarUsuarioInexistente() {
        when(usuarioRepository.findById(usuarioFake.getId())).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () ->
                usuarioService.desativarUsuario(usuarioFake.getId()));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    public void deveLancarExcecao_QuandoTentarDesativarAdmin() {
        Role roleAdmin = new Role();
        roleAdmin.setNome("ADMIN");
        usuarioFake.getRoles().add(roleAdmin);

        when(usuarioRepository.findById(usuarioFake.getId())).thenReturn(Optional.of(usuarioFake));

        assertThrows(OperacaoInvalidaException.class, () ->
                usuarioService.desativarUsuario(usuarioFake.getId()));

        assertTrue(usuarioFake.getAtivo());

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    public void deveCriarUsuarioComSucesso() {
        when(usuarioRepository.existsByEmail(usuarioRequestFake.email())).thenReturn(false);
        when(roleRepository.findByNome("USER")).thenReturn(Optional.of(roleFake));
        when(usuarioMapper.toEntity(usuarioRequestFake)).thenReturn(usuarioFake);
        when(passwordEncoder.encode(usuarioRequestFake.senha())).thenReturn("senha_hash_criptografada");
        when(usuarioRepository.save(usuarioFake)).thenReturn(usuarioFake);
        when(tokenVerificacaoService.gerarToken(eq(usuarioFake), eq(TipoVerificacao.CONFIRMACAO_EMAIL))).thenReturn("TOKEN_QUALQUER");
        when(usuarioMapper.toResponse(usuarioFake)).thenReturn(usuarioResponseFake);

        UsuarioResponse resultado = usuarioService.create(usuarioRequestFake);

        assertNotNull(resultado);
        assertEquals(usuarioResponseFake.id(), resultado.id());
        assertEquals(usuarioResponseFake.email(), resultado.email());

        verify(usuarioRepository, times(1)).save(usuarioFake);
        verify(tokenVerificacaoService, times(1)).gerarToken(usuarioFake, TipoVerificacao.CONFIRMACAO_EMAIL);
        verify(emailService, times(1)).sendEmail(
                eq(usuarioFake.getEmail()),
                eq("FINANCE Controle Financeiro: Confirmação de e-mail"),
                anyString()
        );
    }

    @Test
    public void deveLancarExcecao_QuandoTentarSalvarUsuarioComEmailQueJaExiste() {
        when(usuarioRepository.existsByEmail(usuarioRequestFake.email())).thenReturn(true);

        assertThrows(ConflictException.class, () ->
                usuarioService.create(usuarioRequestFake));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    public void deveLancarExcecao_QuandoTentarSalvarUsuarioComUmaRoleQueNaoExiste() {
        when(usuarioRepository.existsByEmail(usuarioRequestFake.email())).thenReturn(false);
        when(roleRepository.findByNome("USER")).thenReturn(Optional.empty());

        assertThrows(RoleNaoEncontradaException.class, () ->
                usuarioService.create(usuarioRequestFake));

        verify(usuarioRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    public void deveLancarExcecao_QuandoOcorrerErroDeIntegridadeNoBancoAoSalvar() {
        when(usuarioRepository.existsByEmail(usuarioRequestFake.email())).thenReturn(false);
        when(roleRepository.findByNome("USER")).thenReturn(Optional.of(roleFake));
        when(usuarioMapper.toEntity(usuarioRequestFake)).thenReturn(usuarioFake);
        when(passwordEncoder.encode(usuarioRequestFake.senha())).thenReturn("senha_hash_criptografada");

        when(usuarioRepository.save(usuarioFake)).thenThrow(new DataIntegrityViolationException("Erro simulado de constraint no banco"));

        assertThrows(ConflictException.class, () ->
                usuarioService.create(usuarioRequestFake));

        verify(tokenVerificacaoService, never()).gerarToken(any(), any());
        verify(emailService, never()).sendEmail(any(), any(), any());
    }

    @Test
    public void deveAtualizarUsuarioComSucesso() {
        when(usuarioRepository.findById(usuarioFake.getId())).thenReturn(Optional.of(usuarioFake));
        when(usuarioRepository.save(usuarioFake)).thenReturn(usuarioFake);
        when(usuarioMapper.toResponse(usuarioFake)).thenReturn(usuarioResponseFake);

        UsuarioResponse resultado = usuarioService.update(usuarioFake.getId(), requestUpdateFake);
        assertNotNull(resultado);
        assertEquals(usuarioResponseFake.id(), resultado.id());
        assertEquals(usuarioResponseFake.nome(), resultado.nome());

        verify(usuarioRepository, times(1)).save(usuarioFake);
        verify(usuarioMapper, times(1)).atualizarUsuario(requestUpdateFake, usuarioFake);
    }

    @Test
    public void deveLancarExcecao_QuandoTentarAtualizarUsuarioComIdQueNaoExiste() {
        when(usuarioRepository.findById(usuarioFake.getId())).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () ->
                usuarioService.update(usuarioFake.getId(), requestUpdateFake));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    public void deveBuscarUsuarioPorEmailComSucesso() {
        when(usuarioRepository.findByEmail(usuarioResponseFake.email())).thenReturn(Optional.of(usuarioFake));
        when(usuarioMapper.toResponse(usuarioFake)).thenReturn(usuarioResponseFake);

        UsuarioResponse resultado = usuarioService.findByEmail(usuarioResponseFake.email());
        assertNotNull(resultado);
        assertEquals(usuarioResponseFake.email(), resultado.email());


        verify(usuarioRepository, times(1)).findByEmail(usuarioResponseFake.email());
        verify(usuarioMapper, times(1)).toResponse(usuarioFake);
    }

    @Test
    public void deveBuscarUsuarioPorIdComSucesso() {
        when(usuarioRepository.findById(usuarioFake.getId())).thenReturn(Optional.of(usuarioFake));
        when(usuarioMapper.toResponse(usuarioFake)).thenReturn(usuarioResponseFake);

        UsuarioResponse resultado = usuarioService.findById(usuarioFake.getId());
        assertNotNull(resultado);
        assertEquals(usuarioResponseFake.id(), resultado.id());

        verify(usuarioRepository, times(1)).findById(usuarioFake.getId());
        verify(usuarioMapper, times(1)).toResponse(usuarioFake);
    }

    @Test
    public void deveLancarExcecao_QuandoTentarBuscarUsuarioComEmailQueNaoExiste() {
        String emailBusca = usuarioResponseFake.email();

        when(usuarioRepository.findByEmail(emailBusca)).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () ->
                usuarioService.findByEmail(emailBusca));

        verify(usuarioRepository, times(1)).findByEmail(emailBusca);
        verify(usuarioMapper, never()).toResponse(any());
    }

    @Test
    public void deveLancarExcecao_QuandoTentarBuscarUsuarioComIdQueNaoExiste() {
        when(usuarioRepository.findById(usuarioFake.getId())).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () ->
                usuarioService.findById(usuarioFake.getId()));

        verify(usuarioRepository, times(1)).findById(usuarioFake.getId());
        verify(usuarioMapper, never()).toResponse(any());
    }

    @Test
    public void deveExcluirContaComSucesso_QuandoIdExistir() {
        when(usuarioRepository.existsById(usuarioFake.getId())).thenReturn(true);

        usuarioService.excluirConta(usuarioFake.getId());

        verify(usuarioRepository, times(1)).deleteById(usuarioFake.getId());
    }

    @Test
    public void naoDeveFazerNada_QuandoTentarExcluirContaComIdQueNaoExiste() {
        when(usuarioRepository.existsById(usuarioFake.getId())).thenReturn(false);

        usuarioService.excluirConta(usuarioFake.getId());

        verify(usuarioRepository, never()).deleteById(any());
    }

    @Test
    public void deveMarcarEmailComoVerificado() {
        usuarioService.marcarEmailComoVerificado(usuarioFake);

        assertTrue(usuarioFake.getEmailVerificado());

        verify(usuarioRepository, times(1)).save(usuarioFake);
    }
}
