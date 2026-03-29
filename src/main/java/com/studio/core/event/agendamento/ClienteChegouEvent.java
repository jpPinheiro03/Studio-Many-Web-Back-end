package com.studio.core.event.agendamento;

import com.studio.core.event.BaseEvent;
import java.time.LocalDateTime;

public class ClienteChegouEvent extends BaseEvent {

    private final Long clienteId;
    private final String clienteNome;
    private final Long funcionarioId;
    private final String funcionarioNome;
    private final LocalDateTime dataChegada;

    public ClienteChegouEvent(Object source, Long agendamentoId,
                               Long clienteId, String clienteNome,
                               Long funcionarioId, String funcionarioNome,
                               LocalDateTime dataChegada) {
        super(source, "Agendamento", agendamentoId);
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.funcionarioId = funcionarioId;
        this.funcionarioNome = funcionarioNome;
        this.dataChegada = dataChegada;
    }

    public Long getClienteId() { return clienteId; }
    public String getClienteNome() { return clienteNome; }
    public Long getFuncionarioId() { return funcionarioId; }
    public String getFuncionarioNome() { return funcionarioNome; }
    public LocalDateTime getDataChegada() { return dataChegada; }
}
