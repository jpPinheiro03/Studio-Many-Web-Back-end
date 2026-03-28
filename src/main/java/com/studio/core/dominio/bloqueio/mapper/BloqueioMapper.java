package com.studio.core.dominio.bloqueio.mapper;

import com.studio.core.dominio.bloqueio.dto.BloqueioRequestDTO;
import com.studio.core.dominio.bloqueio.dto.BloqueioResponseDTO;
import com.studio.core.dominio.bloqueio.entity.Bloqueio;
import com.studio.core.dominio.funcionario.entity.Funcionario;

public class BloqueioMapper {

    public static Bloqueio toEntity(BloqueioRequestDTO dto, Funcionario func) {
        Bloqueio entity = new Bloqueio();
        entity.setFunc(func);
        entity.setDataInicio(dto.getDataInicio());
        entity.setDataFim(dto.getDataFim());
        entity.setMotivo(dto.getMotivo());
        return entity;
    }

    public static BloqueioResponseDTO toResponse(Bloqueio entity) {
        BloqueioResponseDTO dto = new BloqueioResponseDTO();
        dto.setId(entity.getId());
        if (entity.getFunc() != null) {
            dto.setFunc(new com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO());
            dto.getFunc().setId(entity.getFunc().getId());
            dto.getFunc().setNome(entity.getFunc().getNome());
        }
        dto.setDataInicio(entity.getDataInicio());
        dto.setDataFim(entity.getDataFim());
        dto.setMotivo(entity.getMotivo());
        return dto;
    }
}
