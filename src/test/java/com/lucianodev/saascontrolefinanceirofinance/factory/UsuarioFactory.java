package com.lucianodev.saascontrolefinanceirofinance.factory;

import com.lucianodev.saascontrolefinanceirofinance.dto.request.*;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.UsuarioResponse;
import com.lucianodev.saascontrolefinanceirofinance.entity.Role;
import com.lucianodev.saascontrolefinanceirofinance.entity.Usuario;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UsuarioFactory {

    public static Usuario usuarioValido() {

        Set<Role> roles = new HashSet<>();
        Role roleUser = new Role();
        roleUser.setNome("USER");
        roles.add(roleUser);
        Usuario usuario = new Usuario();
        usuario.setId(UUID.randomUUID());
        usuario.setNome("Luciano Santos");
        usuario.setEmail("santos@email.com");
        usuario.setSenhaHash("$2a$10$XURPShQNCsLjp1ESc2laoO46CGkVvyBJndLn659f51CEU3J29hO76");
        usuario.setEmailVerificado(true);
        usuario.setMoeda("BRL");
        usuario.setFusoHorario("America/Sao_Paulo");
        usuario.setCriadoEm(LocalDateTime.now());
        usuario.setRoles(roles);
        usuario.setAtivo(true);
        return usuario;
    }

    public static UsuarioRequest usuarioRequestValido() {
        Usuario usuario = usuarioValido();

        return new UsuarioRequest(
                usuario.getNome(),
                usuario.getEmail(),
                "Santos45#$@"
        );
    }

    public static UsuarioResponse usuarioResponseValido() {
        Usuario usuario = usuarioValido();
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCriadoEm()
        );
    }

    public static UsuarioUpdateRequest usuarioUpdateRequestValido() {
        return new UsuarioUpdateRequest("Nome Fake Teste");
    }
}
