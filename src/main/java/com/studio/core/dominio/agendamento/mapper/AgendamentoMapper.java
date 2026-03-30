package com.studio.core.dominio.agendamento.mapper;

import com.studio.core.dominio.agendamento.dto.AgendamentoRequestDTO;
import com.studio.core.dominio.agendamento.dto.AgendamentoResponseDTO;
import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.cliente.mapper.ClienteMapper;
import com.studio.core.dominio.funcionario.mapper.FuncionarioMapper;
import com.studio.core.dominio.servico.mapper.ServicoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ClienteMapper.class, ServicoMapper.class, FuncionarioMapper.class})
public interface AgendamentoMapper {

    @Mapping(target = "id", ignore = true) // ID gerado pelo banco
    @Mapping(target = "cliente", ignore = true) // Setado pelo service
    @Mapping(target = "servico", ignore = true) // Setado pelo service
    @Mapping(target = "funcionario", ignore = true) // Setado pelo service
    @Mapping(target = "status", ignore = true) // Setado pelo service
    @Mapping(target = "comprovanteSinal", ignore = true) // Setado depois
    @Mapping(target = "dataConfirmacaoSinal", ignore = true) // Setado pelo service
    @Mapping(target = "dataChegada", ignore = true) // Setado pelo service
    @Mapping(target = "dataFinalizacao", ignore = true) // Setado pelo service
    @Mapping(target = "motivoCancelamento", ignore = true) // Setado pelo service
    @Mapping(target = "agendamentoOriginalId", ignore = true) // Setado pelo premium
    @Mapping(target = "quantidadeReagendamentos", ignore = true) // Setado pelo premium
    @Mapping(target = "taxaCancelamento", ignore = true) // Setado pelo premium
    @Mapping(target = "dataCriacao", ignore = true) // Setado pelo banco
    @Mapping(target = "recorrente", ignore = true) // Setado pelo premium
    @Mapping(target = "frequenciaRecorrencia", ignore = true) // Setado pelo premium
    Agendamento toEntity(AgendamentoRequestDTO dto);

    AgendamentoResponseDTO toResponse(Agendamento entity);
}
