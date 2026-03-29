package com.studio.core.dominio.bloqueio.dto;

import com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BloqueioResponseDTO {

    private Long id;
    private FuncionarioResponseDTO func;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String motivo;
}
