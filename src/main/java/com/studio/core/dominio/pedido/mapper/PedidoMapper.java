package com.studio.core.dominio.pedido.mapper;

import com.studio.core.dominio.cliente.mapper.ClienteMapper;
import com.studio.core.dominio.pedido.dto.PedidoRequestDTO;
import com.studio.core.dominio.pedido.dto.PedidoResponseDTO;
import com.studio.core.dominio.pedido.entity.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ClienteMapper.class, PedidoItemMapper.class})
public interface PedidoMapper {

    @Mapping(target = "id", ignore = true) // ID gerado pelo banco
    @Mapping(target = "cliente", ignore = true) // Setado pelo service
    @Mapping(target = "valorTotal", ignore = true) // Calculado pelo service
    @Mapping(target = "status", ignore = true) // Setado pelo service
    @Mapping(target = "dataPedido", ignore = true) // Setado pelo banco
    Pedido toEntity(PedidoRequestDTO dto);

    PedidoResponseDTO toResponse(Pedido entity);
}
