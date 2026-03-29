package com.studio.core.event.agendamento;

import com.studio.core.event.BaseEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AgendamentoFinalizadoEvent extends BaseEvent {

    private final Long clienteId;
    private final String clienteNome;
    private final Long funcionarioId;
    private final String funcionarioNome;
    private final Long servicoId;
    private final String servicoNome;
    private final BigDecimal valorTotal;
    private final LocalDateTime dataFinalizacao;

    public AgendamentoFinalizadoEvent(Object source, Long agendamentoId,
                                       Long clienteId, String clienteNome,
                                       Long funcionarioId, String funcionarioNome,
                                       Long servicoId, String servicoNome,
                                       BigDecimal valorTotal, LocalDateTime dataFinalizacao) {
        super(source, "Agendamento", agendamentoId);
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.funcionarioId = funcionarioId;
        this.funcionarioNome = funcionarioNome;
        this.servicoId = servicoId;
        this.servicoNome = servicoNome;
        this.valorTotal = valorTotal;
        this.dataFinalizacao = dataFinalizacao;
    }

    public Long getClienteId() { return clienteId; }
    public String getClienteNome() { return clienteNome; }
    public Long getFuncionarioId() { return funcionarioId; }
    public String getFuncionarioNome() { return funcionarioNome; }
    public Long getServicoId() { return servicoId; }
    public String getServicoNome() { return servicoNome; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public LocalDateTime getDataFinalizacao() { return dataFinalizacao; }
}
