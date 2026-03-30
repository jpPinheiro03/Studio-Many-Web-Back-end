package com.studio.core.event.agendamento;

import com.studio.core.event.BaseEvent;

public class AgendamentoCanceladoEvent extends BaseEvent {

    private final Long clienteId;
    private final String clienteNome;
    private final String clienteEmail;
    private final Long funcionarioId;
    private final String funcionarioNome;
    private final String motivo;

    public AgendamentoCanceladoEvent(Object source, Long agendamentoId,
                                      Long clienteId, String clienteNome, String clienteEmail,
                                      Long funcionarioId, String funcionarioNome,
                                      String motivo) {
        super(source, "Agendamento", agendamentoId);
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.clienteEmail = clienteEmail;
        this.funcionarioId = funcionarioId;
        this.funcionarioNome = funcionarioNome;
        this.motivo = motivo;
    }

    public Long getClienteId() { return clienteId; }
    public String getClienteNome() { return clienteNome; }
    public String getClienteEmail() { return clienteEmail; }
    public Long getFuncionarioId() { return funcionarioId; }
    public String getFuncionarioNome() { return funcionarioNome; }
    public String getMotivo() { return motivo; }
}
