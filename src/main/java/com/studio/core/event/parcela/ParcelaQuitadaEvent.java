package com.studio.core.event.parcela;

import com.studio.core.event.BaseEvent;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ParcelaQuitadaEvent extends BaseEvent {

    private final Long agendamentoId;
    private final Long clienteId;
    private final String clienteNome;
    private final Integer numero;
    private final Integer totalParcelas;
    private final BigDecimal valor;
    private final LocalDate dataPagamento;

    public ParcelaQuitadaEvent(Object source, Long parcelaId,
                                Long agendamentoId, Long clienteId, String clienteNome,
                                Integer numero, Integer totalParcelas,
                                BigDecimal valor, LocalDate dataPagamento) {
        super(source, "Parcela", parcelaId);
        this.agendamentoId = agendamentoId;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.numero = numero;
        this.totalParcelas = totalParcelas;
        this.valor = valor;
        this.dataPagamento = dataPagamento;
    }

    public Long getAgendamentoId() { return agendamentoId; }
    public Long getClienteId() { return clienteId; }
    public String getClienteNome() { return clienteNome; }
    public Integer getNumero() { return numero; }
    public Integer getTotalParcelas() { return totalParcelas; }
    public BigDecimal getValor() { return valor; }
    public LocalDate getDataPagamento() { return dataPagamento; }
}
