package com.studio.core.dominio.horario_trabalho.mapper;

import com.studio.core.dominio.horario_trabalho.dto.HorarioTrabalhoRequestDTO;
import com.studio.core.dominio.horario_trabalho.dto.HorarioTrabalhoResponseDTO;
import com.studio.core.dominio.horario_trabalho.entity.HorarioTrabalho;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HorarioTrabalhoMapper {

    HorarioTrabalho toEntity(HorarioTrabalhoRequestDTO dto);

    HorarioTrabalhoResponseDTO toResponse(HorarioTrabalho entity);
}
