package com.studio.core.dominio.pacote.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class PacoteRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Serviço ID é obrigatório")
    private Long servicoId;

    @NotNull(message = "Quantidade de sessões é obrigatória")
    @Positive(message = "Quantidade de sessões deve ser positiva")
    private Integer quantidadeSessoes;

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    private BigDecimal preco;

    @Positive(message = "Validade deve ser positiva")
    private Integer validadeDias;

    private Boolean ativo = true;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Long getServicoId() { return servicoId; }
    public void setServicoId(Long servicoId) { this.servicoId = servicoId; }
    public Integer getQuantidadeSessoes() { return quantidadeSessoes; }
    public void setQuantidadeSessoes(Integer quantidadeSessoes) { this.quantidadeSessoes = quantidadeSessoes; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public Integer getValidadeDias() { return validadeDias; }
    public void setValidadeDias(Integer validadeDias) { this.validadeDias = validadeDias; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
