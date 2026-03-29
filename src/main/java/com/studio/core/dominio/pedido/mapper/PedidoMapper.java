package com.studio.core.dominio.pedido.mapper;

import com.studio.core.dominio.pedido.dto.PedidoRequestDTO;
import com.studio.core.dominio.pedido.dto.PedidoResponseDTO;
import com.studio.core.dominio.pedido.entity.Pedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    Pedido toEntity(PedidoRequestDTO dto);

    PedidoResponseDTO toResponse(Pedido entity);
}
