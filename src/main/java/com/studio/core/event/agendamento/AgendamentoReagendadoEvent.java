package com.studio.core.event.agendamento;

import com.studio.core.event.BaseEvent;

import java.time.LocalDateTime;

public class AgendamentoReagendadoEvent extends BaseEvent {

    private final Long clienteId;
    private final String clienteNome;
    private final String clienteEmail;
    private final LocalDateTime dataOriginal;
    private final LocalDateTime dataNova;
    private final String motivo;
    private final int quantidadeReagendamentos;

    public AgendamentoReagendadoEvent(Object source, Long agendamentoId,
                                       Long clienteId, String clienteNome, String clienteEmail,
                                       LocalDateTime dataOriginal, LocalDateTime dataNova,
                                       String motivo, int quantidadeReagendamentos) {
        super(source, "Agendamento", agendamentoId);
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.clienteEmail = clienteEmail;
        this.dataOriginal = dataOriginal;
        this.dataNova = dataNova;
        this.motivo = motivo;
        this.quantidadeReagendamentos = quantidadeReagendamentos;
    }

    public Long getClienteId() { return clienteId; }
    public String getClienteNome() { return clienteNome; }
    public String getClienteEmail() { return clienteEmail; }
    public LocalDateTime getDataOriginal() { return dataOriginal; }
    public LocalDateTime getDataNova() { return dataNova; }
    public String getMotivo() { return motivo; }
    public int getQuantidadeReagendamentos() { return quantidadeReagendamentos; }
}
