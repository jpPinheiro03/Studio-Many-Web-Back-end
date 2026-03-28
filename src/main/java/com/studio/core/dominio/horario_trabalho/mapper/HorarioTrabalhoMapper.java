package com.studio.core.dominio.horario_trabalho.mapper;

import com.studio.core.dominio.horario_trabalho.dto.HorarioTrabalhoRequestDTO;
import com.studio.core.dominio.horario_trabalho.dto.HorarioTrabalhoResponseDTO;
import com.studio.core.dominio.horario_trabalho.entity.HorarioTrabalho;

public class HorarioTrabalhoMapper {

    public static HorarioTrabalho toEntity(HorarioTrabalhoRequestDTO dto) {
        HorarioTrabalho entity = new HorarioTrabalho();
        entity.setDiaSemana(dto.getDiaSemana());
        entity.setHoraInicio(dto.getHoraInicio());
        entity.setHoraFim(dto.getHoraFim());
        return entity;
    }

    public static HorarioTrabalhoResponseDTO toResponse(HorarioTrabalho entity) {
        HorarioTrabalhoResponseDTO dto = new HorarioTrabalhoResponseDTO();
        dto.setId(entity.getId());
        if (entity.getFunc() != null) {
            dto.setFunc(new com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO());
            dto.getFunc().setId(entity.getFunc().getId());
            dto.getFunc().setNome(entity.getFunc().getNome());
        }
        dto.setDiaSemana(entity.getDiaSemana());
        dto.setHoraInicio(entity.getHoraInicio());
        dto.setHoraFim(entity.getHoraFim());
        return dto;
    }
}
