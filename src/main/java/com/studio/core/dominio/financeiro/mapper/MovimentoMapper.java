package com.studio.core.dominio.financeiro.mapper;

import com.studio.core.dominio.financeiro.entity.Movimento;
import com.studio.core.dominio.movimento.dto.MovimentoRequestDTO;
import com.studio.core.dominio.movimento.dto.MovimentoResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovimentoMapper {

    Movimento toEntity(MovimentoRequestDTO dto);

    MovimentoResponseDTO toResponse(Movimento entity);
}
