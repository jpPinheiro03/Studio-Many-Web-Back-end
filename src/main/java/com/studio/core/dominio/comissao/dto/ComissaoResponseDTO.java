package com.studio.core.dominio.comissao.dto;

import com.studio.core.dominio.agendamento.dto.AgendamentoResponseDTO;
import com.studio.core.dominio.funcionario.dto.FuncionarioResponseDTO;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ComissaoResponseDTO {

    private Long id;
    private FuncionarioResponseDTO func;
    private AgendamentoResponseDTO agendamento;
    private BigDecimal valor;
    private BigDecimal percentual;
    private LocalDate dataComissao;
    private LocalDate dataPagamento;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public FuncionarioResponseDTO getFunc() { return func; }
    public void setFunc(FuncionarioResponseDTO func) { this.func = func; }
    public AgendamentoResponseDTO getAgendamento() { return agendamento; }
    public void setAgendamento(AgendamentoResponseDTO agendamento) { this.agendamento = agendamento; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public BigDecimal getPercentual() { return percentual; }
    public void setPercentual(BigDecimal percentual) { this.percentual = percentual; }
    public LocalDate getDataComissao() { return dataComissao; }
    public void setDataComissao(LocalDate dataComissao) { this.dataComissao = dataComissao; }
    public LocalDate getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public static ComissaoResponseDTO fromEntity(com.studio.core.dominio.comissao.entity.Comissao entity) {
        ComissaoResponseDTO dto = new ComissaoResponseDTO();
        dto.setId(entity.getId());
        if (entity.getFunc() != null) {
            dto.setFunc(FuncionarioResponseDTO.fromEntity(entity.getFunc()));
        }
        dto.setValor(entity.getValor());
        dto.setPercentual(entity.getPercentual());
        dto.setDataComissao(entity.getDataComissao());
        dto.setDataPagamento(entity.getDataPagamento());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        return dto;
    }
}
