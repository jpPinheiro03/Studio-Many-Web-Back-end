package com.studio.core.dominio.lista_espera.mapper;

import com.studio.core.dominio.lista_espera.dto.ListaEsperaRequestDTO;
import com.studio.core.dominio.lista_espera.dto.ListaEsperaResponseDTO;
import com.studio.core.dominio.lista_espera.entity.ListaEspera;

public class ListaEsperaMapper {

    public static ListaEspera toEntity(ListaEsperaRequestDTO dto) {
        ListaEspera entity = new ListaEspera();
        entity.setDataDesejada(dto.getDataDesejada());
        entity.setHorarioDesejado(dto.getHorarioDesejado());
        entity.setObservacoes(dto.getObservacoes());
        return entity;
    }

    public static ListaEsperaResponseDTO toResponse(ListaEspera entity) {
        ListaEsperaResponseDTO dto = new ListaEsperaResponseDTO();
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
        if (entity.getFunc() != null) {
            dto.setFunc(new com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO());
            dto.getFunc().setId(entity.getFunc().getId());
            dto.getFunc().setNome(entity.getFunc().getNome());
        }
        dto.setDataDesejada(entity.getDataDesejada());
        dto.setHorarioDesejado(entity.getHorarioDesejado());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setObservacoes(entity.getObservacoes());
        dto.setDataCadastro(entity.getDataCadastro());
        return dto;
    }
}
