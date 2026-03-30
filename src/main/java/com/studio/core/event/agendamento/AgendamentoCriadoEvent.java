package com.studio.core.event.agendamento;

import com.studio.core.dominio.agendamento.entity.StatusAgendamento;
import com.studio.core.event.BaseEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AgendamentoCriadoEvent extends BaseEvent {

    private final Long clienteId;
    private final String clienteNome;
    private final String clienteEmail;
    private final Long funcionarioId;
    private final String funcionarioNome;
    private final Long servicoId;
    private final String servicoNome;
    private final LocalDateTime dataHoraInicio;
    private final LocalDateTime dataHoraFim;
    private final BigDecimal valorTotal;
    private final BigDecimal valorSinal;

    public AgendamentoCriadoEvent(Object source, Long agendamentoId,
                                   Long clienteId, String clienteNome, String clienteEmail,
                                   Long funcionarioId, String funcionarioNome,
                                   Long servicoId, String servicoNome,
                                   LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim,
                                   BigDecimal valorTotal, BigDecimal valorSinal) {
        super(source, "Agendamento", agendamentoId);
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.clienteEmail = clienteEmail;
        this.funcionarioId = funcionarioId;
        this.funcionarioNome = funcionarioNome;
        this.servicoId = servicoId;
        this.servicoNome = servicoNome;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.valorTotal = valorTotal;
        this.valorSinal = valorSinal;
    }

    public Long getClienteId() { return clienteId; }
    public String getClienteNome() { return clienteNome; }
    public String getClienteEmail() { return clienteEmail; }
    public Long getFuncionarioId() { return funcionarioId; }
    public String getFuncionarioNome() { return funcionarioNome; }
    public Long getServicoId() { return servicoId; }
    public String getServicoNome() { return servicoNome; }
    public LocalDateTime getDataHoraInicio() { return dataHoraInicio; }
    public LocalDateTime getDataHoraFim() { return dataHoraFim; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public BigDecimal getValorSinal() { return valorSinal; }
}
