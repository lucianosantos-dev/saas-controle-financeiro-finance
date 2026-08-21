package com.lucianodev.saascontrolefinanceirofinance.mapper;

import com.lucianodev.saascontrolefinanceirofinance.dto.request.UsuarioRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.request.UsuarioUpdateRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.UsuarioResponse;
import com.lucianodev.saascontrolefinanceirofinance.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UsuarioMapper {

    UsuarioResponse toResponse(Usuario entity);
    Usuario toEntity(UsuarioRequest request);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "senhaHash", ignore = true)
    @Mapping(target = "emailVerificado", ignore = true)
    @Mapping(target = "moeda", ignore = true)
    @Mapping(target = "fusoHorario", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    void atualizarUsuario(UsuarioUpdateRequest request, @MappingTarget Usuario entity);
}
