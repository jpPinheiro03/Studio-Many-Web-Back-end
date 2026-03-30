package com.studio.core.dominio.parcela.mapper;

import com.studio.core.dominio.parcela.dto.ParcelaResponseDTO;
import com.studio.core.dominio.parcela.entity.Parcela;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParcelaMapper {

    ParcelaResponseDTO toResponse(Parcela entity);
}
