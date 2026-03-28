package com.studio.core.dominio.comissao.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "comissoes")
public class Comissao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Funcionario func;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Agendamento agendamento;
    
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;
    
    @Column(name = "percentual", precision = 5, scale = 2)
    private BigDecimal percentual;
    
    @Column(name = "data_comissao")
    private LocalDate dataComissao;
    
    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusComissao status = StatusComissao.PENDENTE;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Funcionario getFunc() { return func; }
    public void setFunc(Funcionario func) { this.func = func; }
    public Agendamento getAgendamento() { return agendamento; }
    public void setAgendamento(Agendamento agendamento) { this.agendamento = agendamento; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public BigDecimal getPercentual() { return percentual; }
    public void setPercentual(BigDecimal percentual) { this.percentual = percentual; }
    public LocalDate getDataComissao() { return dataComissao; }
    public void setDataComissao(LocalDate dataComissao) { this.dataComissao = dataComissao; }
    public LocalDate getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }
    public StatusComissao getStatus() { return status; }
    public void setStatus(StatusComissao status) { this.status = status; }

    public enum StatusComissao {
        PENDENTE,
        PAGA
    }
}
