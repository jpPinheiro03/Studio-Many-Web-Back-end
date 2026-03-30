package com.studio.core.event.agendamento;

import com.studio.core.event.BaseEvent;

public class ComprovanteEnviadoEvent extends BaseEvent {

    private final Long clienteId;
    private final String clienteNome;
    private final String comprovanteCaminho;

    public ComprovanteEnviadoEvent(Object source, Long agendamentoId,
                                    Long clienteId, String clienteNome, String comprovanteCaminho) {
        super(source, "Agendamento", agendamentoId);
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.comprovanteCaminho = comprovanteCaminho;
    }

    public Long getClienteId() { return clienteId; }
    public String getClienteNome() { return clienteNome; }
    public String getComprovanteCaminho() { return comprovanteCaminho; }
}
