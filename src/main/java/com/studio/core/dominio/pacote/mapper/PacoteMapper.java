package com.studio.core.dominio.pacote.mapper;

import com.studio.core.dominio.pacote.dto.PacoteRequestDTO;
import com.studio.core.dominio.pacote.dto.PacoteResponseDTO;
import com.studio.core.dominio.pacote.entity.Pacote;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PacoteMapper {

    Pacote toEntity(PacoteRequestDTO dto);

    PacoteResponseDTO toResponse(Pacote entity);
}
