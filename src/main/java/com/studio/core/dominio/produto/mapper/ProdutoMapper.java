package com.studio.core.dominio.produto.mapper;

import com.studio.core.dominio.produto.dto.ProdutoRequestDTO;
import com.studio.core.dominio.produto.dto.ProdutoResponseDTO;
import com.studio.core.dominio.produto.entity.Produto;

public class ProdutoMapper {

    public static Produto toEntity(ProdutoRequestDTO dto) {
        Produto entity = new Produto();
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        entity.setPreco(dto.getPreco());
        entity.setEstoque(dto.getEstoque());
        entity.setAtivo(dto.getAtivo());
        return entity;
    }

    public static ProdutoResponseDTO toResponse(Produto entity) {
        ProdutoResponseDTO dto = new ProdutoResponseDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setDescricao(entity.getDescricao());
        dto.setPreco(entity.getPreco());
        dto.setEstoque(entity.getEstoque());
        dto.setAtivo(entity.getAtivo());
        dto.setDataCadastro(entity.getDataCadastro());
        return dto;
    }
}
