package com.lucianodev.saascontrolefinanceirofinance.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailRequest(

        @NotBlank(message = "Campo obrigatório")
        @Email(message = "E-mail inválido")
        String email
) {
}
