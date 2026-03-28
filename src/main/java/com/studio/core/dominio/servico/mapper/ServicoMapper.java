package com.studio.core.dominio.servico.mapper;

import com.studio.core.dominio.servico.dto.ServicoRequestDTO;
import com.studio.core.dominio.servico.dto.ServicoResponseDTO;
import com.studio.core.dominio.servico.entity.Servico;

public class ServicoMapper {

    public static Servico toEntity(ServicoRequestDTO dto) {
        Servico entity = new Servico();
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        entity.setPreco(dto.getPreco());
        entity.setDuracaoMinutos(dto.getDuracaoMinutos());
        entity.setAtivo(dto.getAtivo());
        return entity;
    }

    public static ServicoResponseDTO toResponse(Servico entity) {
        ServicoResponseDTO dto = new ServicoResponseDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setDescricao(entity.getDescricao());
        dto.setPreco(entity.getPreco());
        dto.setDuracaoMinutos(entity.getDuracaoMinutos());
        dto.setAtivo(entity.getAtivo());
        return dto;
    }
}
