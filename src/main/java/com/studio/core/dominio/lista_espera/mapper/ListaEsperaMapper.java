package com.studio.core.dominio.lista_espera.mapper;

import com.studio.core.dominio.lista_espera.dto.ListaEsperaRequestDTO;
import com.studio.core.dominio.lista_espera.dto.ListaEsperaResponseDTO;
import com.studio.core.dominio.lista_espera.entity.ListaEspera;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ListaEsperaMapper {

    ListaEspera toEntity(ListaEsperaRequestDTO dto);

    ListaEsperaResponseDTO toResponse(ListaEspera entity);
}
