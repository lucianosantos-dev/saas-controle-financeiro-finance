package com.lucianodev.saascontrolefinanceirofinance.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LoginResponse {

    private String tokenAcesso;
    private Long expiraEm;
}
