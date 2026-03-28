package com.studio.core.dominio.financeiro.mapper;

import com.studio.core.dominio.financeiro.entity.Movimento;
import com.studio.core.dominio.movimento.dto.MovimentoRequestDTO;
import com.studio.core.dominio.movimento.dto.MovimentoResponseDTO;

public class MovimentoMapper {

    public static Movimento toEntity(MovimentoRequestDTO dto) {
        Movimento entity = new Movimento();
        entity.setTipo(Movimento.TipoMovimento.valueOf(dto.getTipo()));
        entity.setReferenciaTipo(dto.getReferenciaTipo());
        entity.setReferenciaId(dto.getReferenciaId());
        entity.setValor(dto.getValor());
        entity.setDataMovimento(dto.getDataMovimento());
        entity.setDescricao(dto.getDescricao());
        return entity;
    }

    public static MovimentoResponseDTO toResponse(Movimento entity) {
        MovimentoResponseDTO dto = new MovimentoResponseDTO();
        dto.setId(entity.getId());
        dto.setTipo(entity.getTipo() != null ? entity.getTipo().name() : null);
        dto.setReferenciaTipo(entity.getReferenciaTipo());
        dto.setReferenciaId(entity.getReferenciaId());
        dto.setValor(entity.getValor());
        dto.setDataMovimento(entity.getDataMovimento());
        dto.setDescricao(entity.getDescricao());
        dto.setDataCadastro(entity.getDataCadastro());
        return dto;
    }
}
