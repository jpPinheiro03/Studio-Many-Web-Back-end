package com.studio.core.dominio.cliente_pacote.mapper;

import com.studio.core.dominio.cliente_pacote.dto.ClientePacoteRequestDTO;
import com.studio.core.dominio.cliente_pacote.dto.ClientePacoteResponseDTO;
import com.studio.core.dominio.cliente_pacote.entity.ClientePacote;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientePacoteMapper {

    ClientePacote toEntity(ClientePacoteRequestDTO dto);

    ClientePacoteResponseDTO toResponse(ClientePacote entity);
}
