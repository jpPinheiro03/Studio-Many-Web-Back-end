package com.studio.core.dominio.bloqueio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BloqueioRequestDTO {

    @NotNull(message = "Funcionário ID é obrigatório")
    @Schema(name = "funcionarioId", example = "1")
    private Long funcionarioId;

    @NotNull(message = "Data de início é obrigatória")
    @Schema(name = "dataInicio", example = "2026-03-29T20:36:32.949Z")
    private LocalDateTime dataInicio;

    @NotNull(message = "Data de fim é obrigatória")
    @Schema(name = "dataFim", example = "2026-06-29T20:36:32.949Z")
    private LocalDateTime dataFim;

    @Schema(name = "motivo", example = "Férias")
    private String motivo;

    public Long getFuncionarioId() {
        return funcionarioId;
    }
}
