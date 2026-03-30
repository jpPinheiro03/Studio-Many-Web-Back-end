package com.studio.core.event.comissao;

import com.studio.core.event.BaseEvent;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ComissaoPagaEvent extends BaseEvent {

    private final Long funcionarioId;
    private final String funcionarioNome;
    private final Long agendamentoId;
    private final BigDecimal valor;
    private final BigDecimal percentual;
    private final LocalDate dataPagamento;

    public ComissaoPagaEvent(Object source, Long comissaoId,
                              Long funcionarioId, String funcionarioNome,
                              Long agendamentoId, BigDecimal valor, BigDecimal percentual,
                              LocalDate dataPagamento) {
        super(source, "Comissao", comissaoId);
        this.funcionarioId = funcionarioId;
        this.funcionarioNome = funcionarioNome;
        this.agendamentoId = agendamentoId;
        this.valor = valor;
        this.percentual = percentual;
        this.dataPagamento = dataPagamento;
    }

    public Long getFuncionarioId() { return funcionarioId; }
    public String getFuncionarioNome() { return funcionarioNome; }
    public Long getAgendamentoId() { return agendamentoId; }
    public BigDecimal getValor() { return valor; }
    public BigDecimal getPercentual() { return percentual; }
    public LocalDate getDataPagamento() { return dataPagamento; }
}
