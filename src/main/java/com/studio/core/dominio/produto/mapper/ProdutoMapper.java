package com.studio.core.dominio.produto.mapper;

import com.studio.core.dominio.produto.dto.ProdutoRequestDTO;
import com.studio.core.dominio.produto.dto.ProdutoResponseDTO;
import com.studio.core.dominio.produto.entity.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    @Mapping(target = "id", ignore = true) // ID gerado pelo banco
    Produto toEntity(ProdutoRequestDTO dto);

    ProdutoResponseDTO toResponse(Produto entity);
}
