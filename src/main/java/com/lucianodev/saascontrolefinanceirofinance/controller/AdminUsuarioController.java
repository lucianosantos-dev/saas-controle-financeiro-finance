package com.lucianodev.saascontrolefinanceirofinance.controller;

import com.lucianodev.saascontrolefinanceirofinance.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/admin/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsuarioController {

    private final UsuarioService service;

    public AdminUsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PatchMapping("{id}/desativar")
    public ResponseEntity<Void> desativarUsuario(@PathVariable UUID id) {
        service.desativarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}/ativar")
    public ResponseEntity<Void> ativarUsuario(@PathVariable UUID id) {
        service.ativarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
