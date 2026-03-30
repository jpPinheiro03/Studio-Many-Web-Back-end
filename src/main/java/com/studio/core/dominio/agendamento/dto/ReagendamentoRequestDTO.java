package com.studio.core.dominio.agendamento.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReagendamentoRequestDTO {

    private LocalDateTime novaDataHoraInicio;
    private LocalDateTime novaDataHoraFim;
    private String motivo;
}
