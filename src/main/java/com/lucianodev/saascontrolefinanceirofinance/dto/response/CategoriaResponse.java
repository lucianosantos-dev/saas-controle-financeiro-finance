package com.lucianodev.saascontrolefinanceirofinance.dto.response;

import com.lucianodev.saascontrolefinanceirofinance.entity.Categoria;
import com.lucianodev.saascontrolefinanceirofinance.enums.TipoCategoria;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoriaResponse(
        UUID id,
        String nome,
        TipoCategoria tipo,
        Boolean ativo,
        LocalDateTime criadoEm,
        Boolean isSistema
) {

    public CategoriaResponse(Categoria cat) {
        this(
                cat.getId(),
                cat.getNome(),
                cat.getTipoCategoria(),
                cat.getAtivo(),
                cat.getCriadoEm(),
                cat.getUsuario() == null
        );
    }
}
