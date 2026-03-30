package com.studio.core.dominio.pedido.mapper;

import com.studio.core.dominio.pedido.dto.PedidoRequestDTO;
import com.studio.core.dominio.pedido.dto.PedidoResponseDTO;
import com.studio.core.dominio.pedido.entity.PedidoItem;
import com.studio.core.dominio.produto.mapper.ProdutoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProdutoMapper.class})
public interface PedidoItemMapper {

    @Mapping(target = "id", ignore = true) // ID gerado pelo banco
    @Mapping(target = "pedido", ignore = true) // Setado pelo service
    @Mapping(target = "produto", ignore = true) // Setado pelo service
    @Mapping(target = "preco", ignore = true) // Setado pelo service
    PedidoItem toEntity(PedidoRequestDTO.PedidoItemRequestDTO dto);

    PedidoResponseDTO.PedidoItemResponseDTO toResponse(PedidoItem entity);
}
