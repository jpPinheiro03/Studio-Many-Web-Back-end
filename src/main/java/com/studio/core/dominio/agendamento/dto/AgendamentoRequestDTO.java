package com.studio.core.dominio.agendamento.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AgendamentoRequestDTO {

    @NotNull(message = "Cliente ID é obrigatório")
    private Long clienteId;

    @NotNull(message = "Serviço ID é obrigatório")
    private Long servicoId;

    @NotNull(message = "Funcionário ID é obrigatório")
    private Long funcionarioId;

    @NotNull(message = "Data/hora de início é obrigatória")
    private LocalDateTime dataHoraInicio;

    @NotNull(message = "Data/hora de fim é obrigatória")
    private LocalDateTime dataHoraFim;

    private BigDecimal valorTotal;
    private BigDecimal valorSinal;
    private Integer quantidadeParcelas = 1;
    private String observacoes;

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public Long getServicoId() { return servicoId; }
    public void setServicoId(Long servicoId) { this.servicoId = servicoId; }
    public Long getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(Long funcionarioId) { this.funcionarioId = funcionarioId; }
    public LocalDateTime getDataHoraInicio() { return dataHoraInicio; }
    public void setDataHoraInicio(LocalDateTime dataHoraInicio) { this.dataHoraInicio = dataHoraInicio; }
    public LocalDateTime getDataHoraFim() { return dataHoraFim; }
    public void setDataHoraFim(LocalDateTime dataHoraFim) { this.dataHoraFim = dataHoraFim; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public BigDecimal getValorSinal() { return valorSinal; }
    public void setValorSinal(BigDecimal valorSinal) { this.valorSinal = valorSinal; }
    public Integer getQuantidadeParcelas() { return quantidadeParcelas; }
    public void setQuantidadeParcelas(Integer quantidadeParcelas) { this.quantidadeParcelas = quantidadeParcelas; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
