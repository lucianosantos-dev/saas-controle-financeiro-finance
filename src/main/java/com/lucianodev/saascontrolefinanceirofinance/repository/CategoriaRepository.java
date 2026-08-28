package com.lucianodev.saascontrolefinanceirofinance.repository;

import com.lucianodev.saascontrolefinanceirofinance.entity.Categoria;
import com.lucianodev.saascontrolefinanceirofinance.entity.Usuario;
import com.lucianodev.saascontrolefinanceirofinance.enums.TipoCategoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
    boolean existsByNomeAndTipoCategoriaAndUsuario(String nome, TipoCategoria tipo, Usuario usuario);

    @Query("SELECT c FROM Categoria c WHERE c.ativo = true AND (c.usuario.id = :idUsuario OR c.usuario IS NULL)")
    Page<Categoria> listarCategoriasUsuario(@Param("idUsuario") UUID idUsuario, Pageable pageable);

    @Query("SELECT c FROM Categoria c WHERE c.tipoCategoria = :tipo AND c.ativo = true AND (c.usuario.id = :idUsuario OR c.usuario IS NULL)")
    List<Categoria> listarCategoriasUsuarioPeloTipo(@Param("idUsuario") UUID idUsuario, @Param("tipo") TipoCategoria tipo);

    @Query("SELECT c FROM Categoria c WHERE c.id = :idCateg AND (c.usuario.id = :idUsuario OR c.usuario IS NULL)")
    Optional<Categoria> buscarPorIdSeguro(@Param("idCateg") UUID idCateg, @Param("idUsuario") UUID idUsuario);
}
