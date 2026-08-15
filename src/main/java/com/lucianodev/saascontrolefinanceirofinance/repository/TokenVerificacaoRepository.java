package com.lucianodev.saascontrolefinanceirofinance.repository;

import com.lucianodev.saascontrolefinanceirofinance.entity.TokenVerificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TokenVerificacaoRepository extends JpaRepository<TokenVerificacao, UUID> {
    Optional<TokenVerificacao> findByToken(String token);
}
