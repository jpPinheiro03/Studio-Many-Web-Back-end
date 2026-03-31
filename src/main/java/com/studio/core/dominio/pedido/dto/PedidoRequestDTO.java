package com.studio.core.dominio.pedido.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.util.List;

@Data
public class PedidoRequestDTO {

    @NotNull(message = "Cliente ID é obrigatório")
    private Long clienteId;

    @Valid
    @NotNull(message = "Itens são obrigatórios")
    private List<PedidoItemRequestDTO> itens;

    @Data
    public static class PedidoItemRequestDTO {
        @NotNull(message = "Produto ID é obrigatório")
        @Schema(example = "1")
        private Long produtoId;

        @NotNull(message = "Quantidade é obrigatória")
        @Positive(message = "Quantidade precisa ser positiva")
        @Schema(example = "5")
        private Integer quantidade;
    }
}
