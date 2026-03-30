package com.studio.core.dominio.horario_trabalho.mapper;

import com.studio.core.dominio.funcionario.mapper.FuncionarioMapper;
import com.studio.core.dominio.horario_trabalho.dto.HorarioTrabalhoRequestDTO;
import com.studio.core.dominio.horario_trabalho.dto.HorarioTrabalhoResponseDTO;
import com.studio.core.dominio.horario_trabalho.entity.HorarioTrabalho;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {FuncionarioMapper.class})
public interface HorarioTrabalhoMapper {

    @Mapping(target = "id", ignore = true) // ID gerado pelo banco
    @Mapping(target = "funcionario", ignore = true) // Setado pelo service
    @Mapping(target = "funcionarioId", source = "funcionarioId") // DTO.funcionarioId → entity.funcionarioId
    HorarioTrabalho toEntity(HorarioTrabalhoRequestDTO dto);

    HorarioTrabalhoResponseDTO toResponse(HorarioTrabalho entity);
}
