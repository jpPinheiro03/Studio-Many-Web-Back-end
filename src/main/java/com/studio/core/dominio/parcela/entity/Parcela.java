package com.studio.core.dominio.parcela.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.agendamento.entity.Agendamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity // Esta classe vira uma tabela no banco de dados
@Table(name = "parcelas") // Nome da tabela no banco (snake_case)
@Getter @Setter // Lombok: gera getters e setters automaticamente
public class Parcela {

    @Id // Chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento pelo banco
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Muitas parcelas apontam para 1 agendamento (carregamento sob demanda)
    @JoinColumn(name = "agendamento_id", nullable = false) // Nome da coluna FK obrigatória
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private Agendamento agendamento;

    @Column(nullable = false) // Coluna obrigatória (NOT NULL)
    private Integer numero;

    @Column(precision = 10, scale = 2, nullable = false) // Número decimal obrigatório (DECIMAL(10,2))
    private BigDecimal valor;

    @Column(name = "data_vencimento", nullable = false) // Coluna obrigatória com nome customizado
    private LocalDate dataVencimento;

    @Column(name = "data_pagamento") // Nome da coluna no banco (snake_case)
    private LocalDate dataPagamento;

    @Enumerated(EnumType.STRING) // Armazena o enum como texto no banco
    @Column(length = 20) // Tamanho máximo da coluna (VARCHAR(20))
    private StatusParcela status = StatusParcela.PENDENTE;

    public enum StatusParcela {
        PENDENTE,
        QUITADA,
        VENCIDA,
        CANCELADA
    }
}
