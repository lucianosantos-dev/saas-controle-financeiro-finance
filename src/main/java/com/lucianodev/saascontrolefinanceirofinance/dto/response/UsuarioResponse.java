package com.lucianodev.saascontrolefinanceirofinance.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record UsuarioResponse(
        UUID id,
        String nome,
        String email,
        LocalDateTime criadoEm
) {
}
