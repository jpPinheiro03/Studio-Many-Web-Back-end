package com.studio.core.dominio.pedido.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class PedidoRequestDTO {

    @NotNull(message = "Cliente ID é obrigatório")
    private Long clienteId;

    @NotNull(message = "Itens são obrigatórios")
    private List<PedidoItemRequestDTO> itens;

    @Data
    public static class PedidoItemRequestDTO {
        @NotNull(message = "Produto ID é obrigatório")
        private Long produtoId;

        @NotNull(message = "Quantidade é obrigatória")
        private Integer quantidade;
    }
}
