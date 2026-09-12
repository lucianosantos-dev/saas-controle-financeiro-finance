package com.lucianodev.saascontrolefinanceirofinance.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CartaoUpdateRequest(

        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 60, message = "O nome não pode ter mais que 60 caracteres.")
        String nome,

        @NotNull(message = "O limite de crédito é obrigatório.")
        @Positive(message = "O limite deve ser maior que 0.")
        BigDecimal limiteCredito,

        @NotNull(message = "O dia do fechamento é obrigatório.")
        @Min(value = 1, message = "O dia do fechamento deve ser no mínimo 1.")
        @Max(value = 31, message = "O dia do fechamento deve ser no máximo 31.")
        Integer diaFechamento,

        @NotNull(message = "O dia do vencimento é obrigatório.")
        @Min(value = 1, message = "O dia do vencimento deve ser no mínimo 1.")
        @Max(value = 31, message = "O dia do vencimento deve ser no máximo 31.")
        Integer diaVencimento
) {
}
