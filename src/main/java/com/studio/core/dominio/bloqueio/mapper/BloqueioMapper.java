package com.studio.core.dominio.bloqueio.mapper;

import com.studio.core.dominio.bloqueio.dto.BloqueioRequestDTO;
import com.studio.core.dominio.bloqueio.dto.BloqueioResponseDTO;
import com.studio.core.dominio.bloqueio.entity.Bloqueio;
import com.studio.core.dominio.funcionario.mapper.FuncionarioMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {FuncionarioMapper.class})
public interface BloqueioMapper {

    @Mapping(target = "id", ignore = true) // ID gerado pelo banco
    @Mapping(target = "funcionario", ignore = true) // Setado pelo service
    @Mapping(source = "funcionarioId", target = "funcionarioId") // DTO.funcionarioId → entity.funcionarioId
    Bloqueio toEntity(BloqueioRequestDTO dto);

    BloqueioResponseDTO toResponse(Bloqueio entity);
}
