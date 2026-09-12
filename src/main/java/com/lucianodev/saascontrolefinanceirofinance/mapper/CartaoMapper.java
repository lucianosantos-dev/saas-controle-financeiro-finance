package com.lucianodev.saascontrolefinanceirofinance.mapper;

import com.lucianodev.saascontrolefinanceirofinance.dto.request.CartaoRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.request.CartaoUpdateRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.CartaoResponse;
import com.lucianodev.saascontrolefinanceirofinance.entity.Cartao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CartaoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    Cartao toEntity(CartaoRequest request);
    CartaoResponse toResponse(Cartao entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    void updateCartao(CartaoUpdateRequest request,  @MappingTarget Cartao entity);
}
