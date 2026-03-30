package com.studio.core.dominio.agendamento.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HistoricoNoShowDTO {

    private Long clienteId;
    private String clienteNome;
    private int totalNoShows;
    private int totalAgendamentos;
    private int percentualNoShow;
    private LocalDateTime ultimoNoShow;
}
