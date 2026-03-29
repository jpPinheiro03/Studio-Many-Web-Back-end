package com.studio.core.dominio.produto.mapper;

import com.studio.core.dominio.produto.dto.ProdutoRequestDTO;
import com.studio.core.dominio.produto.dto.ProdutoResponseDTO;
import com.studio.core.dominio.produto.entity.Produto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    Produto toEntity(ProdutoRequestDTO dto);

    ProdutoResponseDTO toResponse(Produto entity);
}
