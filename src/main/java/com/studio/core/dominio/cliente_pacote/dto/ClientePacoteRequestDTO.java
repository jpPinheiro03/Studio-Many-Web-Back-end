package com.studio.core.dominio.cliente_pacote.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClientePacoteRequestDTO {

    @NotNull(message = "Cliente ID é obrigatório")
    private Long clienteId;

    @NotNull(message = "Pacote ID é obrigatório")
    private Long pacoteId;
}
