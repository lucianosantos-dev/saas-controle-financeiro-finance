package com.lucianodev.saascontrolefinanceirofinance.controller;

import com.lucianodev.saascontrolefinanceirofinance.dto.request.UsuarioRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.request.UsuarioUpdateRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.UsuarioResponse;
import com.lucianodev.saascontrolefinanceirofinance.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> save(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse response = service.create(request);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/perfil")
    public ResponseEntity<UsuarioResponse> update(@Valid @RequestBody UsuarioUpdateRequest request,
                                                  JwtAuthenticationToken token

    ) {
        UUID identificador = UUID.fromString(token.getName());
        return ResponseEntity.ok(service.update(identificador, request));
    }

    @GetMapping
    public ResponseEntity<UsuarioResponse> findByEmail(@RequestParam String email) {
        return ResponseEntity.ok(service.findByEmail(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("/perfil/me")
    public ResponseEntity<Void> deleteById(JwtAuthenticationToken token) {
        UUID idUsuario = UUID.fromString(token.getName());
        service.excluirConta(idUsuario);
        return ResponseEntity.noContent().build();
    }
}
