package com.studio.core.dominio.pedido.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.cliente.entity.Cliente;
import com.studio.core.dominio.produto.entity.Produto;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cliente cliente;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<PedidoItem> itens = new ArrayList<>();
    
    @Column(precision = 10, scale = 2)
    private BigDecimal valorTotal;
    
    @Enumerated(EnumType.STRING)
    private StatusPedido status = StatusPedido.PENDENTE;
    
    @Column(name = "data_pedido")
    private LocalDateTime dataPedido = LocalDateTime.now();
    
    @Transient
    private Long clienteId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Long getClienteId() { return cliente != null ? cliente.getId() : clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public List<PedidoItem> getItens() { return itens; }
    public void setItens(List<PedidoItem> itens) { this.itens = itens; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }
    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }
    
    public void addItem(Produto produto, Integer quantidade) {
        PedidoItem item = new PedidoItem();
        item.setPedido(this);
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setPreco(produto.getPreco());
        this.itens.add(item);
    }

    public enum StatusPedido {
        PENDENTE,
        PAGO,
        CANCELADO
    }
}
