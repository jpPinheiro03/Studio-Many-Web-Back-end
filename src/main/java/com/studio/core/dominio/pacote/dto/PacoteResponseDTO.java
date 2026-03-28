package com.studio.core.dominio.pacote.dto;

import com.studio.core.dominio.servico.dto.ServicoResponseDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PacoteResponseDTO {

    private Long id;
    private String nome;
    private ServicoResponseDTO servico;
    private Integer quantidadeSessoes;
    private BigDecimal preco;
    private Integer validadeDias;
    private Boolean ativo;
    private LocalDateTime dataCadastro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public ServicoResponseDTO getServico() { return servico; }
    public void setServico(ServicoResponseDTO servico) { this.servico = servico; }
    public Integer getQuantidadeSessoes() { return quantidadeSessoes; }
    public void setQuantidadeSessoes(Integer quantidadeSessoes) { this.quantidadeSessoes = quantidadeSessoes; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public Integer getValidadeDias() { return validadeDias; }
    public void setValidadeDias(Integer validadeDias) { this.validadeDias = validadeDias; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
}
