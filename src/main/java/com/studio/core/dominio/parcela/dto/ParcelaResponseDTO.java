package com.studio.core.dominio.parcela.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ParcelaResponseDTO {

    private Long id;
    private Integer numero;
    private BigDecimal valor;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
    public LocalDate getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public static ParcelaResponseDTO fromEntity(com.studio.core.dominio.parcela.entity.Parcela entity) {
        ParcelaResponseDTO dto = new ParcelaResponseDTO();
        dto.setId(entity.getId());
        dto.setNumero(entity.getNumero());
        dto.setValor(entity.getValor());
        dto.setDataVencimento(entity.getDataVencimento());
        dto.setDataPagamento(entity.getDataPagamento());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        return dto;
    }
}
