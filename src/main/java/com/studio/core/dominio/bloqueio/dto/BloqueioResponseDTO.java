package com.studio.core.dominio.bloqueio.dto;

import com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO;
import java.time.LocalDateTime;

public class BloqueioResponseDTO {

    private Long id;
    private FuncionarioResponseDTO func;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String motivo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public FuncionarioResponseDTO getFunc() { return func; }
    public void setFunc(FuncionarioResponseDTO func) { this.func = func; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }
    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
