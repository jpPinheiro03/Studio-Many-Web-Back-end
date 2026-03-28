package com.studio.core.dominio.pacote.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.servico.entity.Servico;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pacotes")
public class Pacote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servico_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Servico servico;
    
    @Column(name = "quantidade_sessoes", nullable = false)
    private Integer quantidadeSessoes;
    
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal preco;
    
    @Column(name = "validade_dias")
    private Integer validadeDias;
    
    private Boolean ativo = true;
    
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro = LocalDateTime.now();
    
    @Transient
    private Long servicoId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Servico getServico() { return servico; }
    public void setServico(Servico servico) { this.servico = servico; }
    public Long getServicoId() { return servico != null ? servico.getId() : servicoId; }
    public void setServicoId(Long servicoId) { this.servicoId = servicoId; }
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
