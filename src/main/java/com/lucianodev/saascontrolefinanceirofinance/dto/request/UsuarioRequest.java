package com.lucianodev.saascontrolefinanceirofinance.dto.request;

import com.lucianodev.saascontrolefinanceirofinance.validation.SenhaForte;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank(message = "Nome é obrigatório")
        String nome,
        @NotBlank(message = "E-mail é obrigatório")
        @Email
        String email,
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, max = 64, message = "Senha deve ter entre 8 e 64 caracteres")
        @SenhaForte
        String senha
) {
}
