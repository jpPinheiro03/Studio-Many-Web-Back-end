package com.studio.core.dominio.cliente.mapper;

import com.studio.core.dominio.cliente.dto.ClienteRequestDTO;
import com.studio.core.dominio.cliente.dto.ClienteResponseDTO;
import com.studio.core.dominio.cliente.entity.Cliente;

public class ClienteMapper {

    public static Cliente toEntity(ClienteRequestDTO dto) {
        Cliente entity = new Cliente();
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setTelefone(dto.getTelefone());
        entity.setCpf(dto.getCpf());
        entity.setEndereco(dto.getEndereco());
        entity.setObservacoes(dto.getObservacoes());
        entity.setAtivo(dto.getAtivo());
        return entity;
    }

    public static ClienteResponseDTO toResponse(Cliente entity) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setEmail(entity.getEmail());
        dto.setTelefone(entity.getTelefone());
        dto.setCpf(entity.getCpf());
        dto.setEndereco(entity.getEndereco());
        dto.setObservacoes(entity.getObservacoes());
        dto.setEstagioFunil(entity.getEstagioFunil());
        dto.setDataCadastro(entity.getDataCadastro());
        dto.setAtivo(entity.getAtivo());
        return dto;
    }
}
