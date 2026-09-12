package com.lucianodev.saascontrolefinanceirofinance.controller;

import com.lucianodev.saascontrolefinanceirofinance.dto.request.CartaoRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.request.CartaoUpdateRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.CartaoResponse;
import com.lucianodev.saascontrolefinanceirofinance.service.CartaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    private final CartaoService service;

    public CartaoController(CartaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CartaoResponse> novoCartao(@RequestBody @Valid CartaoRequest request, Authentication authentication) {
        UUID idUsuario = UUID.fromString(authentication.getName());

        CartaoResponse response = service.create(request, idUsuario);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartaoResponse> update(@RequestBody @Valid CartaoUpdateRequest request,
                                                 @PathVariable UUID id,
                                                 Authentication auth) {
        UUID idUsuario = UUID.fromString(auth.getName());

        return ResponseEntity.ok(service.update(request, id, idUsuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartaoResponse> findById(@PathVariable UUID id, Authentication auth) {
        UUID idUsuario = UUID.fromString(auth.getName());

        return ResponseEntity.ok(service.findById(id, idUsuario));
    }

    @GetMapping
    public ResponseEntity<List<CartaoResponse>> findAll(Authentication auth) {
        UUID idUsuario = UUID.fromString(auth.getName());

        return ResponseEntity.ok(service.findAll(idUsuario));
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable UUID id, Authentication auth) {
        UUID idUsuario = UUID.fromString(auth.getName());

        service.ativarCartao(id, idUsuario);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable UUID id, Authentication auth) {
        UUID idUsuario = UUID.fromString(auth.getName());

        service.desativarCartao(id, idUsuario);

        return ResponseEntity.noContent().build();
    }
}
