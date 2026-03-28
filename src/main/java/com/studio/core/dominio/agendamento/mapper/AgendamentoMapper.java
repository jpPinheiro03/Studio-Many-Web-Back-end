package com.studio.core.dominio.agendamento.mapper;

import com.studio.core.dominio.agendamento.dto.AgendamentoRequestDTO;
import com.studio.core.dominio.agendamento.dto.AgendamentoResponseDTO;
import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.cliente.entity.Cliente;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.dominio.servico.entity.Servico;

public class AgendamentoMapper {

    public static Agendamento toEntity(AgendamentoRequestDTO dto, Cliente cliente, Servico servico, Funcionario funcionario) {
        Agendamento entity = new Agendamento();
        entity.setCliente(cliente);
        entity.setServico(servico);
        entity.setFuncionario(funcionario);
        entity.setDataHoraInicio(dto.getDataHoraInicio());
        entity.setDataHoraFim(dto.getDataHoraFim());
        entity.setValorTotal(dto.getValorTotal());
        entity.setValorSinal(dto.getValorSinal());
        entity.setQuantidadeParcelas(dto.getQuantidadeParcelas());
        entity.setObservacoes(dto.getObservacoes());
        return entity;
    }

    public static AgendamentoResponseDTO toResponse(Agendamento entity) {
        AgendamentoResponseDTO dto = new AgendamentoResponseDTO();
        dto.setId(entity.getId());
        if (entity.getCliente() != null) {
            dto.setCliente(new com.studio.core.dominio.cliente.dto.ClienteResponseDTO());
            dto.getCliente().setId(entity.getCliente().getId());
            dto.getCliente().setNome(entity.getCliente().getNome());
            dto.getCliente().setTelefone(entity.getCliente().getTelefone());
        }
        if (entity.getServico() != null) {
            dto.setServico(new com.studio.core.dominio.servico.dto.ServicoResponseDTO());
            dto.getServico().setId(entity.getServico().getId());
            dto.getServico().setNome(entity.getServico().getNome());
        }
        if (entity.getFuncionario() != null) {
            dto.setFuncionario(new com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO());
            dto.getFuncionario().setId(entity.getFuncionario().getId());
            dto.getFuncionario().setNome(entity.getFuncionario().getNome());
        }
        dto.setDataHoraInicio(entity.getDataHoraInicio());
        dto.setDataHoraFim(entity.getDataHoraFim());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setValorTotal(entity.getValorTotal());
        dto.setValorSinal(entity.getValorSinal());
        dto.setQuantidadeParcelas(entity.getQuantidadeParcelas());
        dto.setMotivoCancelamento(entity.getMotivoCancelamento());
        dto.setObservacoes(entity.getObservacoes());
        return dto;
    }
}
