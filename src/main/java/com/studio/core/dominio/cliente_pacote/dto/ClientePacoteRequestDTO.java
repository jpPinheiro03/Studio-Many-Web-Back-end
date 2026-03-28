package com.studio.core.dominio.cliente_pacote.dto;

import jakarta.validation.constraints.NotNull;

public class ClientePacoteRequestDTO {

    @NotNull(message = "Cliente ID é obrigatório")
    private Long clienteId;

    @NotNull(message = "Pacote ID é obrigatório")
    private Long pacoteId;

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public Long getPacoteId() { return pacoteId; }
    public void setPacoteId(Long pacoteId) { this.pacoteId = pacoteId; }
}
