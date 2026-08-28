package com.lucianodev.saascontrolefinanceirofinance.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaUpdateRequest(
        @NotBlank(message = "Nome da categoria é  obrigatório")
        @Size(min = 5, max = 60, message = "Nome da categoria deve conter entre 5 caracteres e um maximo de 60 caracteres")
        String nome
) {
}
