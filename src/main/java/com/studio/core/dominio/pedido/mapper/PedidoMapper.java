package com.studio.core.dominio.pedido.mapper;

import com.studio.core.dominio.pedido.dto.PedidoRequestDTO;
import com.studio.core.dominio.pedido.dto.PedidoResponseDTO;
import com.studio.core.dominio.pedido.entity.Pedido;
import com.studio.core.dominio.pedido.entity.PedidoItem;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoMapper {

    public static Pedido toEntity(PedidoRequestDTO dto) {
        Pedido entity = new Pedido();
        return entity;
    }

    public static PedidoResponseDTO toResponse(Pedido entity) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(entity.getId());
        if (entity.getCliente() != null) {
            dto.setCliente(new com.studio.core.dominio.cliente.dto.ClienteResponseDTO());
            dto.getCliente().setId(entity.getCliente().getId());
            dto.getCliente().setNome(entity.getCliente().getNome());
            dto.getCliente().setTelefone(entity.getCliente().getTelefone());
        }
        dto.setItens(entity.getItens().stream()
            .map(PedidoMapper::toItemResponse)
            .collect(Collectors.toList()));
        dto.setValorTotal(entity.getValorTotal());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setDataPedido(entity.getDataPedido());
        return dto;
    }

    private static PedidoResponseDTO.PedidoItemResponseDTO toItemResponse(PedidoItem entity) {
        PedidoResponseDTO.PedidoItemResponseDTO dto = new PedidoResponseDTO.PedidoItemResponseDTO();
        dto.setId(entity.getId());
        if (entity.getProduto() != null) {
            dto.setProduto(new com.studio.core.dominio.produto.dto.ProdutoResponseDTO());
            dto.getProduto().setId(entity.getProduto().getId());
            dto.getProduto().setNome(entity.getProduto().getNome());
        }
        dto.setQuantidade(entity.getQuantidade());
        dto.setPreco(entity.getPreco());
        return dto;
    }
}
