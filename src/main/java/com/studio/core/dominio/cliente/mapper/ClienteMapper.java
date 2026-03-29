package com.studio.core.dominio.cliente.mapper;

import com.studio.core.dominio.cliente.dto.ClienteRequestDTO;
import com.studio.core.dominio.cliente.dto.ClienteResponseDTO;
import com.studio.core.dominio.cliente.entity.Cliente;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // MapStruct: gera implementação do mapper automaticamente para o Spring
public interface ClienteMapper {

    Cliente toEntity(ClienteRequestDTO dto);

    ClienteResponseDTO toResponse(Cliente entity);
}
