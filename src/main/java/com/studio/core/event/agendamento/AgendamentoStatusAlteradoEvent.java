package com.studio.core.event.agendamento;

import com.studio.core.dominio.agendamento.entity.StatusAgendamento;
import com.studio.core.event.BaseEvent;

public class AgendamentoStatusAlteradoEvent extends BaseEvent {

    private final StatusAgendamento statusAnterior;
    private final StatusAgendamento statusNovo;
    private final Long clienteId;
    private final String clienteNome;
    private final String clienteEmail;
    private final Long funcionarioId;
    private final String funcionarioNome;

    public AgendamentoStatusAlteradoEvent(Object source, Long agendamentoId,
                                           StatusAgendamento statusAnterior, StatusAgendamento statusNovo,
                                           Long clienteId, String clienteNome, String clienteEmail,
                                           Long funcionarioId, String funcionarioNome) {
        super(source, "Agendamento", agendamentoId);
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.clienteEmail = clienteEmail;
        this.funcionarioId = funcionarioId;
        this.funcionarioNome = funcionarioNome;
    }

    public StatusAgendamento getStatusAnterior() { return statusAnterior; }
    public StatusAgendamento getStatusNovo() { return statusNovo; }
    public Long getClienteId() { return clienteId; }
    public String getClienteNome() { return clienteNome; }
    public String getClienteEmail() { return clienteEmail; }
    public Long getFuncionarioId() { return funcionarioId; }
    public String getFuncionarioNome() { return funcionarioNome; }
}
