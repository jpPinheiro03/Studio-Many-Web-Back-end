package com.studio.core.dominio.servico.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity // Esta classe vira uma tabela no banco de dados
@Table(name = "servicos") // Nome da tabela no banco (snake_case)
@Getter @Setter // Lombok: gera getters e setters automaticamente
public class Servico {

    @Id // Chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento pelo banco
    private Long id;

    @Column(nullable = false) // Coluna obrigatória (NOT NULL)
    private String nome;

    @Column(columnDefinition = "TEXT") // Texto longo (sem limite de caracteres)
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2) // Número decimal obrigatório (DECIMAL(10,2))
    private BigDecimal preco;

    @Column(name = "duracao_minutos") // Nome da coluna no banco (snake_case)
    private Integer duracaoMinutos;

    private Boolean ativo = true; // Coluna opcional com valor padrão

    @Column(name = "confirmacao_automatica") // Nome da coluna no banco (snake_case)
    private Boolean confirmacaoAutomatica = false;
}
