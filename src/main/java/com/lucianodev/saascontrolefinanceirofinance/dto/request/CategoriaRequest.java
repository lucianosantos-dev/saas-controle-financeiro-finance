package com.lucianodev.saascontrolefinanceirofinance.dto.request;

import com.lucianodev.saascontrolefinanceirofinance.enums.TipoCategoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record CategoriaRequest(
        @NotBlank(message = "Nome da categoria é  obrigatório")
        @Size(min = 5, max = 60, message = "Nome da categoria deve conter entre 5 caracteres e um maximo de 60 caracteres")
        String nome,
        @NotNull(message = "Tipo da categoria é  obrigatório")
        TipoCategoria tipo
) {
}
