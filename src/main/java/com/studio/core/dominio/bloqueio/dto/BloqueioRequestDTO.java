package com.studio.core.dominio.bloqueio.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class BloqueioRequestDTO {

    @NotNull(message = "Funcionário ID é obrigatório")
    private Long funcId;

    @NotNull(message = "Data de início é obrigatória")
    private LocalDateTime dataInicio;

    @NotNull(message = "Data de fim é obrigatória")
    private LocalDateTime dataFim;

    private String motivo;

    public Long getFuncionarioId() { return funcId; }
    public void setFuncionarioId(Long funcId) { this.funcId = funcId; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }
    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
