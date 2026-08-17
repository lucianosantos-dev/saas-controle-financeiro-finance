package com.lucianodev.saascontrolefinanceirofinance.controller;

import com.lucianodev.saascontrolefinanceirofinance.dto.CustomErrorDto;
import com.lucianodev.saascontrolefinanceirofinance.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
 feature/login
import org.springframework.security.authentication.BadCredentialsException;

 develop
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<CustomErrorDto> conflict(ConflictException e, HttpServletRequest request) {
        return builderResponse(HttpStatus.CONFLICT, e.getMessage(), request);
    }

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<CustomErrorDto> email(EmailException e, HttpServletRequest request) {
        return builderResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request);
    }

    @ExceptionHandler(TokenExpiradoException.class)
    public ResponseEntity<CustomErrorDto> tokenExpirado(TokenExpiradoException e, HttpServletRequest request) {
        return builderResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), request);
    }

    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<CustomErrorDto> tokenInvalido(TokenInvalidoException e, HttpServletRequest request) {
        return builderResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), request);
    }

    @ExceptionHandler(TokenUsadoException.class)
    public ResponseEntity<CustomErrorDto> tokenUsado(TokenUsadoException e, HttpServletRequest request) {
        return builderResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

feature/login
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomErrorDto> badCredentials(BadCredentialsException e, HttpServletRequest request) {
        return builderResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), request);
    }

    @ExceptionHandler(EmailNaoVerificadoException.class)
    public ResponseEntity<CustomErrorDto> emailNaoVerificado(EmailNaoVerificadoException e, HttpServletRequest request) {
        return builderResponse(HttpStatus.FORBIDDEN, e.getMessage(), request);
    }


 develop
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorDto> exception(Exception e, HttpServletRequest request) {
        return builderResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro inesperado no servidor", request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorDto> methodArgument(MethodArgumentNotValidException e, HttpServletRequest request) {
        String msgLimpa = e.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .findFirst()
                .orElse("Erro de validação nos campos.");
        return builderResponse(HttpStatus.UNPROCESSABLE_CONTENT, msgLimpa, request);
    }


    private ResponseEntity<CustomErrorDto> builderResponse(HttpStatus status, String msg, HttpServletRequest request) {
        CustomErrorDto errorDto = new CustomErrorDto(Instant.now(), status.value(), msg, request.getRequestURI());
        return ResponseEntity.status(status).body(errorDto);
    }
}
