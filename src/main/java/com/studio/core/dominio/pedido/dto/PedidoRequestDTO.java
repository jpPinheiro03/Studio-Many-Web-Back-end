package com.studio.core.dominio.pedido.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class PedidoRequestDTO {

    @NotNull(message = "Cliente ID é obrigatório")
    private Long clienteId;

    @NotNull(message = "Itens são obrigatórios")
    private List<PedidoItemRequestDTO> itens;

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public List<PedidoItemRequestDTO> getItens() { return itens; }
    public void setItens(List<PedidoItemRequestDTO> itens) { this.itens = itens; }

    public static class PedidoItemRequestDTO {
        @NotNull(message = "Produto ID é obrigatório")
        private Long produtoId;

        @NotNull(message = "Quantidade é obrigatória")
        private Integer quantidade;

        public Long getProdutoId() { return produtoId; }
        public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }
        public Integer getQuantidade() { return quantidade; }
        public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    }
}
