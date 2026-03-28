package com.studio.core.dominio.movimento.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MovimentoResponseDTO {

    private Long id;
    private String tipo;
    private String referenciaTipo;
    private Long referenciaId;
    private BigDecimal valor;
    private LocalDate dataMovimento;
    private String descricao;
    private LocalDateTime dataCadastro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getReferenciaTipo() { return referenciaTipo; }
    public void setReferenciaTipo(String referenciaTipo) { this.referenciaTipo = referenciaTipo; }
    public Long getReferenciaId() { return referenciaId; }
    public void setReferenciaId(Long referenciaId) { this.referenciaId = referenciaId; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public LocalDate getDataMovimento() { return dataMovimento; }
    public void setDataMovimento(LocalDate dataMovimento) { this.dataMovimento = dataMovimento; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public static MovimentoResponseDTO fromEntity(com.studio.core.dominio.financeiro.entity.Movimento entity) {
        MovimentoResponseDTO dto = new MovimentoResponseDTO();
        dto.setId(entity.getId());
        dto.setTipo(entity.getTipo() != null ? entity.getTipo().name() : null);
        dto.setReferenciaTipo(entity.getReferenciaTipo());
        dto.setReferenciaId(entity.getReferenciaId());
        dto.setValor(entity.getValor());
        dto.setDataMovimento(entity.getDataMovimento());
        dto.setDescricao(entity.getDescricao());
        dto.setDataCadastro(entity.getDataCadastro());
        return dto;
    }
}
