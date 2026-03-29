package com.studio.core.dominio.produto.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity // Esta classe vira uma tabela no banco de dados
@Table(name = "produtos") // Nome da tabela no banco (snake_case)
@Getter @Setter // Lombok: gera getters e setters automaticamente
public class Produto {

    @Id // Chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento pelo banco
    private Long id;

    @Column(nullable = false) // Coluna obrigatória (NOT NULL)
    private String nome;

    private String descricao; // Coluna opcional (pode ser nula)

    @Column(precision = 10, scale = 2) // Número decimal (DECIMAL(10,2))
    private BigDecimal preco;

    private Integer estoque = 0; // Coluna opcional com valor padrão

    private Boolean ativo = true; // Coluna opcional com valor padrão

    @Column(name = "data_cadastro") // Nome da coluna no banco (snake_case)
    private LocalDateTime dataCadastro = LocalDateTime.now();
}
