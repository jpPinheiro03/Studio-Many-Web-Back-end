package com.studio.core.dominio.cliente_pacote.mapper;

import com.studio.core.dominio.cliente.mapper.ClienteMapper;
import com.studio.core.dominio.cliente_pacote.dto.ClientePacoteRequestDTO;
import com.studio.core.dominio.cliente_pacote.dto.ClientePacoteResponseDTO;
import com.studio.core.dominio.cliente_pacote.entity.ClientePacote;
import com.studio.core.dominio.pacote.mapper.PacoteMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ClienteMapper.class, PacoteMapper.class})
public interface ClientePacoteMapper {

    @Mapping(target = "id", ignore = true) // ID gerado pelo banco
    @Mapping(target = "cliente", ignore = true) // Setado pelo service
    @Mapping(target = "pacote", ignore = true) // Setado pelo service
    @Mapping(target = "sessoesUsadas", ignore = true) // Valor padrão 0
    @Mapping(target = "status", ignore = true) // Setado pelo service
    @Mapping(target = "dataCompra", ignore = true) // Setado pelo banco
    ClientePacote toEntity(ClientePacoteRequestDTO dto);

    @Mapping(target = "sessoesRestantes", ignore = true) // Campo computado (calculado no service)
    ClientePacoteResponseDTO toResponse(ClientePacote entity);
}
