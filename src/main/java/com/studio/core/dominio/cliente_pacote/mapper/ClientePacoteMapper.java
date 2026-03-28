package com.studio.core.dominio.cliente_pacote.mapper;

import com.studio.core.dominio.cliente_pacote.dto.ClientePacoteRequestDTO;
import com.studio.core.dominio.cliente_pacote.dto.ClientePacoteResponseDTO;
import com.studio.core.dominio.cliente_pacote.entity.ClientePacote;
import com.studio.core.dominio.cliente.entity.Cliente;
import com.studio.core.dominio.pacote.entity.Pacote;

public class ClientePacoteMapper {

    public static ClientePacote toEntity(ClientePacoteRequestDTO dto, Cliente cliente, Pacote pacote) {
        ClientePacote entity = new ClientePacote();
        entity.setCliente(cliente);
        entity.setPacote(pacote);
        if (pacote != null && pacote.getValidadeDias() != null) {
            entity.setDataValidade(java.time.LocalDate.now().plusDays(pacote.getValidadeDias()));
        }
        return entity;
    }

    public static ClientePacoteResponseDTO toResponse(ClientePacote entity) {
        ClientePacoteResponseDTO dto = new ClientePacoteResponseDTO();
        dto.setId(entity.getId());
        if (entity.getCliente() != null) {
            dto.setCliente(new com.studio.core.dominio.cliente.dto.ClienteResponseDTO());
            dto.getCliente().setId(entity.getCliente().getId());
            dto.getCliente().setNome(entity.getCliente().getNome());
            dto.getCliente().setTelefone(entity.getCliente().getTelefone());
        }
        if (entity.getPacote() != null) {
            dto.setPacote(new com.studio.core.dominio.pacote.dto.PacoteResponseDTO());
            dto.getPacote().setId(entity.getPacote().getId());
            dto.getPacote().setNome(entity.getPacote().getNome());
            dto.getPacote().setQuantidadeSessoes(entity.getPacote().getQuantidadeSessoes());
            Integer restantes = entity.getPacote().getQuantidadeSessoes() - entity.getSessoesUsadas();
            dto.setSessoesRestantes(restantes);
        }
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setDataCompra(entity.getDataCompra());
        dto.setDataValidade(entity.getDataValidade());
        return dto;
    }
}
