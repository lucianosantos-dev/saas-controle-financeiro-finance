package com.lucianodev.saascontrolefinanceirofinance.factory;

import com.lucianodev.saascontrolefinanceirofinance.dto.request.AlterarSenhaRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.request.EmailRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.request.LoginRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.request.NovaSenhaRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.LoginResponse;
import com.lucianodev.saascontrolefinanceirofinance.entity.Usuario;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.lucianodev.saascontrolefinanceirofinance.factory.UsuarioFactory.usuarioValido;

public class AuthFactory {

    public static LoginRequest loginRequestValido() {
        Usuario usuario = usuarioValido();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(usuario.getEmail());
        loginRequest.setSenha("Santos45#$@");
        return loginRequest;
    }

    public static LoginResponse loginResponseValido() {
        long expiraEm = 600L;
        LocalDateTime expiracao = LocalDateTime.now().plusSeconds(expiraEm);

        return LoginResponse.builder()
                .tokenAcesso(UUID.randomUUID().toString())
                .expiraEm(expiraEm)
                .build();
    }


    public static NovaSenhaRequest novaSenhaRequest() {
        return new NovaSenhaRequest(
                "tokens339rmvmvfGSRnxssk0#4",
                "NovaSenha556687@.2"
        );
    }

    public static AlterarSenhaRequest alterarSenhaRequestValido() {
        Usuario usuario = usuarioValido();

        return new AlterarSenhaRequest(
                "Santos45#$@",
                "NovaSenha556687@.2"
        );
    }

    public static EmailRequest emailRequestValido() {
        Usuario usuario = usuarioValido();
        return new EmailRequest(
                usuario.getEmail()
        );
    }
}
