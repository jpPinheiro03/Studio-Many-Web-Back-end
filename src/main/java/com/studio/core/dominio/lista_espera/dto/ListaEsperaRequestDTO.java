package com.studio.core.dominio.lista_espera.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class ListaEsperaRequestDTO {

    @NotNull(message = "Cliente ID é obrigatório")
    private Long clienteId;

    @NotNull(message = "Serviço ID é obrigatório")
    private Long servicoId;

    private Long funcId;
    private LocalDate dataDesejada;
    private LocalTime horarioDesejado;
    private String observacoes;

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public Long getServicoId() { return servicoId; }
    public void setServicoId(Long servicoId) { this.servicoId = servicoId; }
    public Long getFuncionarioId() { return funcId; }
    public void setFuncionarioId(Long funcId) { this.funcId = funcId; }
    public LocalDate getDataDesejada() { return dataDesejada; }
    public void setDataDesejada(LocalDate dataDesejada) { this.dataDesejada = dataDesejada; }
    public LocalTime getHorarioDesejado() { return horarioDesejado; }
    public void setHorarioDesejado(LocalTime horarioDesejado) { this.horarioDesejado = horarioDesejado; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
