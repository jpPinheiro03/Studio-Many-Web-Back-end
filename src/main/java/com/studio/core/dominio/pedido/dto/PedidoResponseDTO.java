package com.studio.core.dominio.pedido.dto;

import com.studio.core.dominio.cliente.dto.ClienteResponseDTO;
import com.studio.core.dominio.produto.dto.ProdutoResponseDTO;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoResponseDTO {

    private Long id;
    private ClienteResponseDTO cliente;
    private List<PedidoItemResponseDTO> itens;
    private BigDecimal valorTotal;
    private String status;
    private LocalDateTime dataPedido;

    @Data
    public static class PedidoItemResponseDTO {
        private Long id;
        private ProdutoResponseDTO produto;
        private Integer quantidade;
        private BigDecimal preco;
    }
}
