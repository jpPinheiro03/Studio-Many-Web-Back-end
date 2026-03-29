package com.studio.core.dominio.bloqueio.mapper;

import com.studio.core.dominio.bloqueio.dto.BloqueioRequestDTO;
import com.studio.core.dominio.bloqueio.dto.BloqueioResponseDTO;
import com.studio.core.dominio.bloqueio.entity.Bloqueio;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BloqueioMapper {

    Bloqueio toEntity(BloqueioRequestDTO dto);

    BloqueioResponseDTO toResponse(Bloqueio entity);
}
