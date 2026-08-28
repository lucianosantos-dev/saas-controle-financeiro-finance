package com.lucianodev.saascontrolefinanceirofinance.controller;

import com.lucianodev.saascontrolefinanceirofinance.dto.request.CategoriaRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.request.CategoriaUpdateRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.CategoriaDetalheResponse;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.CategoriaListaResponse;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.CategoriaResponse;
import com.lucianodev.saascontrolefinanceirofinance.enums.TipoCategoria;
import com.lucianodev.saascontrolefinanceirofinance.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> novaCategoria(@RequestBody @Valid CategoriaRequest request, Authentication authentication) {
        UUID idUsuario = UUID.fromString(authentication.getName());

        CategoriaResponse response = service.create(idUsuario, request);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{idCateg}")
    public ResponseEntity<CategoriaResponse> atualizarCategoriaUsuario(
            @PathVariable UUID idCateg,
            @RequestBody @Valid CategoriaUpdateRequest request,
            Authentication authentication
    ) {
        UUID idUsuario = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(service.update(idCateg, idUsuario, request));
    }

    @GetMapping
    public ResponseEntity<Page<CategoriaListaResponse>> listarTodas(@PageableDefault(
            size = 5,
            page = 0,
            sort = "nome",
            direction = Sort.Direction.ASC
    ) Pageable pageable, Authentication authentication) {

        UUID idUsuarioLogado = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(service.findAll(idUsuarioLogado, pageable));
    }

    @GetMapping("/filtro")
    public ResponseEntity<List<CategoriaDetalheResponse>> listarPorTipo(@RequestParam TipoCategoria tipo, Authentication authentication) {
        UUID idUsuarioLogado = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(service.listarPorTipo(idUsuarioLogado, tipo));
    }

    @GetMapping("/{idCateg}")
    public ResponseEntity<CategoriaResponse> buscarPorId(@PathVariable UUID idCateg, Authentication authentication) {
        UUID idUsuario = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(service.findById(idCateg, idUsuario));
    }
}
