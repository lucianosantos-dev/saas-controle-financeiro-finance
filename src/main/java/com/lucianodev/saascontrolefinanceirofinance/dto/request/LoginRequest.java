package com.lucianodev.saascontrolefinanceirofinance.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "E-mail obrigatório para login")
    private String email;

    @NotBlank(message = "Senha obrigatória para login")
    private String senha;

}
