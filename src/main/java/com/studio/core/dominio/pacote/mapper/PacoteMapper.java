package com.studio.core.dominio.pacote.mapper;

import com.studio.core.dominio.pacote.dto.PacoteRequestDTO;
import com.studio.core.dominio.pacote.dto.PacoteResponseDTO;
import com.studio.core.dominio.pacote.entity.Pacote;

public class PacoteMapper {

    public static Pacote toEntity(PacoteRequestDTO dto) {
        Pacote entity = new Pacote();
        entity.setNome(dto.getNome());
        entity.setQuantidadeSessoes(dto.getQuantidadeSessoes());
        entity.setPreco(dto.getPreco());
        entity.setValidadeDias(dto.getValidadeDias());
        entity.setAtivo(dto.getAtivo());
        return entity;
    }

    public static PacoteResponseDTO toResponse(Pacote entity) {
        PacoteResponseDTO dto = new PacoteResponseDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        if (entity.getServico() != null) {
            dto.setServico(new com.studio.core.dominio.servico.dto.ServicoResponseDTO());
            dto.getServico().setId(entity.getServico().getId());
            dto.getServico().setNome(entity.getServico().getNome());
        }
        dto.setQuantidadeSessoes(entity.getQuantidadeSessoes());
        dto.setPreco(entity.getPreco());
        dto.setValidadeDias(entity.getValidadeDias());
        dto.setAtivo(entity.getAtivo());
        dto.setDataCadastro(entity.getDataCadastro());
        return dto;
    }
}
