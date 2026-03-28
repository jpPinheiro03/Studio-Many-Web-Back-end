package com.studio.core.dominio.agendamento.dto;

import com.studio.core.dominio.cliente.dto.ClienteResponseDTO;
import com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO;
import com.studio.core.dominio.servico.dto.ServicoResponseDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AgendamentoResponseDTO {

    private Long id;
    private ClienteResponseDTO cliente;
    private ServicoResponseDTO servico;
    private FuncionarioResponseDTO funcionario;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private String status;
    private BigDecimal valorTotal;
    private BigDecimal valorSinal;
    private Integer quantidadeParcelas;
    private String motivoCancelamento;
    private String observacoes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ClienteResponseDTO getCliente() { return cliente; }
    public void setCliente(ClienteResponseDTO cliente) { this.cliente = cliente; }
    public ServicoResponseDTO getServico() { return servico; }
    public void setServico(ServicoResponseDTO servico) { this.servico = servico; }
    public FuncionarioResponseDTO getFuncionario() { return funcionario; }
    public void setFuncionario(FuncionarioResponseDTO funcionario) { this.funcionario = funcionario; }
    public LocalDateTime getDataHoraInicio() { return dataHoraInicio; }
    public void setDataHoraInicio(LocalDateTime dataHoraInicio) { this.dataHoraInicio = dataHoraInicio; }
    public LocalDateTime getDataHoraFim() { return dataHoraFim; }
    public void setDataHoraFim(LocalDateTime dataHoraFim) { this.dataHoraFim = dataHoraFim; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public BigDecimal getValorSinal() { return valorSinal; }
    public void setValorSinal(BigDecimal valorSinal) { this.valorSinal = valorSinal; }
    public Integer getQuantidadeParcelas() { return quantidadeParcelas; }
    public void setQuantidadeParcelas(Integer quantidadeParcelas) { this.quantidadeParcelas = quantidadeParcelas; }
    public String getMotivoCancelamento() { return motivoCancelamento; }
    public void setMotivoCancelamento(String motivoCancelamento) { this.motivoCancelamento = motivoCancelamento; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public static AgendamentoResponseDTO fromEntity(com.studio.core.dominio.agendamento.entity.Agendamento entity) {
        AgendamentoResponseDTO dto = new AgendamentoResponseDTO();
        dto.setId(entity.getId());
        if (entity.getCliente() != null) {
            dto.setCliente(ClienteResponseDTO.fromEntity(entity.getCliente()));
        }
        if (entity.getServico() != null) {
            dto.setServico(ServicoResponseDTO.fromEntity(entity.getServico()));
        }
        if (entity.getFuncionario() != null) {
            dto.setFuncionario(FuncionarioResponseDTO.fromEntity(entity.getFuncionario()));
        }
        dto.setDataHoraInicio(entity.getDataHoraInicio());
        dto.setDataHoraFim(entity.getDataHoraFim());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setValorTotal(entity.getValorTotal());
        dto.setValorSinal(entity.getValorSinal());
        dto.setQuantidadeParcelas(entity.getQuantidadeParcelas());
        dto.setMotivoCancelamento(entity.getMotivoCancelamento());
        dto.setObservacoes(entity.getObservacoes());
        return dto;
    }
}
