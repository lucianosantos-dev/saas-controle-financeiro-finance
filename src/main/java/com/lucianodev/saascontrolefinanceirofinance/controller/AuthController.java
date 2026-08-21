package com.lucianodev.saascontrolefinanceirofinance.controller;

import com.lucianodev.saascontrolefinanceirofinance.dto.request.EmailRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.request.LoginRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.request.NovaSenhaRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.LoginResponse;
import com.lucianodev.saascontrolefinanceirofinance.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/confirmar-email")
    public ResponseEntity<Void> confirmarEmail(@RequestParam String token) {
        authService.confirmarEmail(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/solicitar-redefinicao-senha")
    public ResponseEntity<Map<String, String>> solicitarRedefinicaoSenha(@Valid @RequestBody EmailRequest dto) {
        authService.solicitarRedefinicao(dto);

        Map<String, String> resposta = Map.of(
                "Mensagem", "Se o e-mail existir na nossa base, as intruções de redefinição foram enviadas."
        );
        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<Map<String, String>> redefinirSenha(@Valid @RequestBody NovaSenhaRequest request) {
        authService.redefinirSenha(request);

        Map<String, String> resposta = Map.of(
                "Mensagem", "Ação realizada com sucesso."
        );
        return ResponseEntity.ok(resposta);
    }
}
