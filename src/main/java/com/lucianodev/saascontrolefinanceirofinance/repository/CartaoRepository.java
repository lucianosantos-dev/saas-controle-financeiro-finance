package com.lucianodev.saascontrolefinanceirofinance.repository;

import com.lucianodev.saascontrolefinanceirofinance.entity.Cartao;
import com.lucianodev.saascontrolefinanceirofinance.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartaoRepository extends JpaRepository<Cartao, UUID> {

    boolean existsByNomeAndUsuarioAndAtivoTrue(String nome, Usuario usuario);
    boolean existsByNomeAndUsuarioAndAtivoTrueAndIdNot(String nome, Usuario usuario, UUID idCartao);

    Optional<Cartao> findByIdAndUsuarioIdAndAtivoTrue(UUID id,UUID idUsuario);

    List<Cartao> findByUsuarioIdAndAtivoTrue(UUID idUsuario);

    Optional<Cartao> findByIdAndUsuarioId(UUID id, UUID idUsuario);
}
