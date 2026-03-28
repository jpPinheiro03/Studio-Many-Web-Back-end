package com.studio.core.dominio.parcela.mapper;

import com.studio.core.dominio.parcela.dto.ParcelaResponseDTO;
import com.studio.core.dominio.parcela.entity.Parcela;

public class ParcelaMapper {

    public static ParcelaResponseDTO toResponse(Parcela entity) {
        ParcelaResponseDTO dto = new ParcelaResponseDTO();
        dto.setId(entity.getId());
        dto.setNumero(entity.getNumero());
        dto.setValor(entity.getValor());
        dto.setDataVencimento(entity.getDataVencimento());
        dto.setDataPagamento(entity.getDataPagamento());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        return dto;
    }
}
