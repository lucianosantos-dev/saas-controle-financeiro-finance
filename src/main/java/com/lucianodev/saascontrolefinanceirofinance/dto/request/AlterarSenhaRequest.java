package com.lucianodev.saascontrolefinanceirofinance.dto.request;

import com.lucianodev.saascontrolefinanceirofinance.validation.SenhaForte;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AlterarSenhaRequest(

        @NotBlank(message = "Campo obrigatório")
        String senhaAtual,

        @NotBlank(message = "campo Obrigatório")
        @Size(min = 8, max = 64, message = "Senha deve ter entre 8 e 64 caracteres")
        @SenhaForte
        String novaSenha
) {
}
