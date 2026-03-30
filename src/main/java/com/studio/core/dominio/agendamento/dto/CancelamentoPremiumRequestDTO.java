package com.studio.core.dominio.agendamento.dto;

import lombok.Data;

@Data
public class CancelamentoPremiumRequestDTO {

    private String motivo;
    private String canceladoPor;
}
