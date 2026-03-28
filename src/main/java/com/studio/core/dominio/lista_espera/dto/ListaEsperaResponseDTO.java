package com.studio.core.dominio.lista_espera.dto;

import com.studio.core.dominio.cliente.dto.ClienteResponseDTO;
import com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO;
import com.studio.core.dominio.servico.dto.ServicoResponseDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ListaEsperaResponseDTO {

    private Long id;
    private ClienteResponseDTO cliente;
    private ServicoResponseDTO servico;
    private FuncionarioResponseDTO func;
    private LocalDate dataDesejada;
    private LocalTime horarioDesejado;
    private String status;
    private String observacoes;
    private LocalDateTime dataCadastro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ClienteResponseDTO getCliente() { return cliente; }
    public void setCliente(ClienteResponseDTO cliente) { this.cliente = cliente; }
    public ServicoResponseDTO getServico() { return servico; }
    public void setServico(ServicoResponseDTO servico) { this.servico = servico; }
    public FuncionarioResponseDTO getFunc() { return func; }
    public void setFunc(FuncionarioResponseDTO func) { this.func = func; }
    public LocalDate getDataDesejada() { return dataDesejada; }
    public void setDataDesejada(LocalDate dataDesejada) { this.dataDesejada = dataDesejada; }
    public LocalTime getHorarioDesejado() { return horarioDesejado; }
    public void setHorarioDesejado(LocalTime horarioDesejado) { this.horarioDesejado = horarioDesejado; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public static ListaEsperaResponseDTO fromEntity(com.studio.core.dominio.lista_espera.entity.ListaEspera entity) {
        ListaEsperaResponseDTO dto = new ListaEsperaResponseDTO();
        dto.setId(entity.getId());
        if (entity.getCliente() != null) {
            dto.setCliente(ClienteResponseDTO.fromEntity(entity.getCliente()));
        }
        if (entity.getServico() != null) {
            dto.setServico(ServicoResponseDTO.fromEntity(entity.getServico()));
        }
        if (entity.getFunc() != null) {
            dto.setFunc(FuncionarioResponseDTO.fromEntity(entity.getFunc()));
        }
        dto.setDataDesejada(entity.getDataDesejada());
        dto.setHorarioDesejado(entity.getHorarioDesejado());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setObservacoes(entity.getObservacoes());
        dto.setDataCadastro(entity.getDataCadastro());
        return dto;
    }
}
