package com.lucianodev.saascontrolefinanceirofinance.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UsuarioUpdateRequest(
        @NotBlank(message = "Nome é obrigatório")
        String nome
) {
}
