package com.studio.core.dominio.pedido.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.produto.entity.Produto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity // Esta classe vira uma tabela no banco de dados
@Table(name = "pedido_itens") // Nome da tabela no banco (snake_case)
@Getter @Setter // Lombok: gera getters e setters automaticamente
public class PedidoItem {

    @Id // Chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento pelo banco
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos itens apontam para 1 pedido (carregamento sob demanda)
    @JoinColumn(name = "pedido_id", nullable = false) // Nome da coluna FK obrigatória
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos itens apontam para 1 produto (carregamento sob demanda)
    @JoinColumn(name = "produto_id", nullable = false) // Nome da coluna FK obrigatória
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private Produto produto;

    @Column(nullable = false) // Coluna obrigatória (NOT NULL)
    private Integer quantidade;

    @Column(name = "preco_unitario", precision = 10, scale = 2, nullable = false) // Número decimal obrigatório (DECIMAL(10,2))
    private BigDecimal preco;

    @Transient // Campo não persistido no banco (só existe em memória)
    private Long produtoId;

    @Transient // Campo não persistido no banco (só existe em memória)
    private Long pedidoId;
}
