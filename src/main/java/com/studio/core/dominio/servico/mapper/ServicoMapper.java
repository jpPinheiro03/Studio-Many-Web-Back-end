package com.studio.core.dominio.servico.mapper;

import com.studio.core.dominio.servico.dto.ServicoRequestDTO;
import com.studio.core.dominio.servico.dto.ServicoResponseDTO;
import com.studio.core.dominio.servico.entity.Servico;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServicoMapper {

    Servico toEntity(ServicoRequestDTO dto);

    ServicoResponseDTO toResponse(Servico entity);
}
