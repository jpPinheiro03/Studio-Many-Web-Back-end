package com.studio.core.dominio.pedido.dto;

import com.studio.core.dominio.cliente.dto.ClienteResponseDTO;
import com.studio.core.dominio.produto.dto.ProdutoResponseDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponseDTO {

    private Long id;
    private ClienteResponseDTO cliente;
    private List<PedidoItemResponseDTO> itens;
    private BigDecimal valorTotal;
    private String status;
    private LocalDateTime dataPedido;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ClienteResponseDTO getCliente() { return cliente; }
    public void setCliente(ClienteResponseDTO cliente) { this.cliente = cliente; }
    public List<PedidoItemResponseDTO> getItens() { return itens; }
    public void setItens(List<PedidoItemResponseDTO> itens) { this.itens = itens; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }

    public static class PedidoItemResponseDTO {
        private Long id;
        private ProdutoResponseDTO produto;
        private Integer quantidade;
        private BigDecimal preco;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public ProdutoResponseDTO getProduto() { return produto; }
        public void setProduto(ProdutoResponseDTO produto) { this.produto = produto; }
        public Integer getQuantidade() { return quantidade; }
        public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
        public BigDecimal getPreco() { return preco; }
        public void setPreco(BigDecimal preco) { this.preco = preco; }
    }
}
