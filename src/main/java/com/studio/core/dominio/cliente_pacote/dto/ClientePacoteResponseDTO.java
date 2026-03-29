package com.studio.core.dominio.cliente_pacote.dto;

import com.studio.core.dominio.cliente.dto.ClienteResponseDTO;
import com.studio.core.dominio.pacote.dto.PacoteResponseDTO;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ClientePacoteResponseDTO {

    private Long id;
    private ClienteResponseDTO cliente;
    private PacoteResponseDTO pacote;
    private Integer sessoesRestantes;
    private String status;
    private LocalDateTime dataCompra;
    private LocalDate dataValidade;
}
