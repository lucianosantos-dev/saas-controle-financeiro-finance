package com.lucianodev.saascontrolefinanceirofinance.dto.response;

import com.lucianodev.saascontrolefinanceirofinance.entity.Categoria;
import com.lucianodev.saascontrolefinanceirofinance.enums.TipoCategoria;

import java.util.UUID;

public record CategoriaListaResponse(
        UUID id,
        String nome,
        TipoCategoria tipo,
        Boolean ativo,
        Boolean isSistema
) {
    public CategoriaListaResponse(Categoria cat) {
        this(
                cat.getId(),
                cat.getNome(),
                cat.getTipoCategoria(),
                cat.getAtivo(),
                cat.getUsuario() == null
        );
    }
}
