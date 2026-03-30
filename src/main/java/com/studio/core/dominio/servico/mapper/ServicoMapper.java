package com.studio.core.dominio.servico.mapper;

import com.studio.core.dominio.servico.dto.ServicoRequestDTO;
import com.studio.core.dominio.servico.dto.ServicoResponseDTO;
import com.studio.core.dominio.servico.entity.Servico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServicoMapper {

    @Mapping(target = "id", ignore = true) // ID gerado pelo banco
    Servico toEntity(ServicoRequestDTO dto);

    ServicoResponseDTO toResponse(Servico entity);
}
