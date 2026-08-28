package com.lucianodev.saascontrolefinanceirofinance.mapper;

import com.lucianodev.saascontrolefinanceirofinance.dto.request.CategoriaRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.request.CategoriaUpdateRequest;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.CategoriaDetalheResponse;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.CategoriaListaResponse;
import com.lucianodev.saascontrolefinanceirofinance.dto.response.CategoriaResponse;
import com.lucianodev.saascontrolefinanceirofinance.entity.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoriaMapper {

   @Mapping(source = "tipoCategoria", target = "tipo")
   @Mapping(target = "isSistema", expression = "java(entity.getUsuario() == null)")
   CategoriaResponse toResponse(Categoria entity);

   @Mapping(source = "tipo", target = "tipoCategoria")
   @Mapping(target = "id", ignore = true)
   @Mapping(target = "ativo", ignore = true)
   @Mapping(target = "usuario", ignore = true)
   @Mapping(target = "criadoEm", ignore = true)
   Categoria toEntity(CategoriaRequest request);

   @Mapping(source = "tipoCategoria", target = "tipo")
   @Mapping(target = "isSistema", expression = "java(entity.getUsuario() == null)")
   CategoriaListaResponse toListaResponse(Categoria entity);
   CategoriaDetalheResponse toCategoriaDetalheResponse(Categoria entity);

   @Mapping(target = "id", ignore = true)
   @Mapping(target = "criadoEm", ignore = true)
   @Mapping(target = "ativo", ignore = true)
   @Mapping(target = "usuario", ignore = true)
   @Mapping(target = "tipoCategoria", ignore = true)
   void updateCategoria(CategoriaUpdateRequest request, @MappingTarget Categoria entity);
}
