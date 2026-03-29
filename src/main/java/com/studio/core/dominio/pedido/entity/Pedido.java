package com.studio.core.dominio.pedido.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.cliente.entity.Cliente;
import com.studio.core.dominio.produto.entity.Produto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity // Esta classe vira uma tabela no banco de dados
@Table(name = "pedidos") // Nome da tabela no banco (snake_case)
@Getter @Setter // Lombok: gera getters e setters automaticamente
public class Pedido {

    @Id // Chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento pelo banco
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos pedidos apontam para 1 cliente (carregamento sob demanda)
    @JoinColumn(name = "cliente_id", nullable = false) // Nome da coluna FK obrigatória
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true) // 1 pedido tem muitos itens (cascade: operações em cascata, orphanRemoval: remove itens órfãos)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private List<PedidoItem> itens = new ArrayList<>();

    @Column(precision = 10, scale = 2) // Número decimal (DECIMAL(10,2))
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING) // Armazena o enum como texto no banco
    private StatusPedido status = StatusPedido.PENDENTE;

    @Column(name = "data_pedido") // Nome da coluna no banco (snake_case)
    private LocalDateTime dataPedido = LocalDateTime.now();

    @Transient // Campo não persistido no banco (só existe em memória)
    private Long clienteId;

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
