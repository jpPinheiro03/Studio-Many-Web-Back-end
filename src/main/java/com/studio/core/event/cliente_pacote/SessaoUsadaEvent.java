package com.studio.core.event.cliente_pacote;

import com.studio.core.event.BaseEvent;

public class SessaoUsadaEvent extends BaseEvent {

    private final Long clienteId;
    private final String clienteNome;
    private final Long pacoteId;
    private final String pacoteNome;
    private final int sessoesUsadas;
    private final int totalSessoes;

    public SessaoUsadaEvent(Object source, Long clientePacoteId,
                             Long clienteId, String clienteNome,
                             Long pacoteId, String pacoteNome,
                             int sessoesUsadas, int totalSessoes) {
        super(source, "ClientePacote", clientePacoteId);
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.pacoteId = pacoteId;
        this.pacoteNome = pacoteNome;
        this.sessoesUsadas = sessoesUsadas;
        this.totalSessoes = totalSessoes;
    }

    public Long getClienteId() { return clienteId; }
    public String getClienteNome() { return clienteNome; }
    public Long getPacoteId() { return pacoteId; }
    public String getPacoteNome() { return pacoteNome; }
    public int getSessoesUsadas() { return sessoesUsadas; }
    public int getTotalSessoes() { return totalSessoes; }
}
