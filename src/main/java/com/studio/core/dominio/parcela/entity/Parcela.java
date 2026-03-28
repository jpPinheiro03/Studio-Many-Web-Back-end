package com.studio.core.dominio.parcela.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.agendamento.entity.Agendamento;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "parcelas")
public class Parcela {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Agendamento agendamento;
    
    @Column(nullable = false)
    private Integer numero;
    
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;
    
    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;
    
    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusParcela status = StatusParcela.PENDENTE;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Agendamento getAgendamento() { return agendamento; }
    public void setAgendamento(Agendamento agendamento) { this.agendamento = agendamento; }
    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
    public LocalDate getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }
    public StatusParcela getStatus() { return status; }
    public void setStatus(StatusParcela status) { this.status = status; }

    public enum StatusParcela {
        PENDENTE,
        QUITADA,
        VENCIDA
    }
}
