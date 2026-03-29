package com.studio.core.dominio.agendamento.mapper;

import com.studio.core.dominio.agendamento.dto.AgendamentoRequestDTO;
import com.studio.core.dominio.agendamento.dto.AgendamentoResponseDTO;
import com.studio.core.dominio.agendamento.entity.Agendamento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // MapStruct: gera implementação do mapper automaticamente para o Spring
public interface AgendamentoMapper {

    Agendamento toEntity(AgendamentoRequestDTO dto);

    @Mapping(source = "cliente.id", target = "cliente.id") // Mapeia o campo cliente.id da origem para o destino
    @Mapping(source = "cliente.nome", target = "cliente.nome") // Mapeia o campo cliente.nome da origem para o destino
    @Mapping(source = "cliente.telefone", target = "cliente.telefone") // Mapeia o campo cliente.telefone da origem para o destino
    @Mapping(source = "servico.id", target = "servico.id") // Mapeia o campo servico.id da origem para o destino
    @Mapping(source = "servico.nome", target = "servico.nome") // Mapeia o campo servico.nome da origem para o destino
    @Mapping(source = "funcionario.id", target = "funcionario.id") // Mapeia o campo funcionario.id da origem para o destino
    @Mapping(source = "funcionario.nome", target = "funcionario.nome") // Mapeia o campo funcionario.nome da origem para o destino
    @Mapping(source = "status", target = "status") // Mapeia o campo status da origem para o destino
    AgendamentoResponseDTO toResponse(Agendamento entity);
}
