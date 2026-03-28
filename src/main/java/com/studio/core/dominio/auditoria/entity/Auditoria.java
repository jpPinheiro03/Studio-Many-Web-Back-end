package com.studio.core.dominio.auditoria.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
public class Auditoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "entidade", nullable = false)
    private String entidade;
    
    @Column(name = "entidade_id", nullable = false)
    private Long entidadeId;
    
    @Column(nullable = false)
    private String acao;
    
    @Column(columnDefinition = "TEXT")
    private String dadosAnteriores;
    
    @Column(columnDefinition = "TEXT")
    private String dadosNovos;
    
    @Column(name = "usuario")
    private String usuario;
    
    @Column(name = "data_acao", nullable = false)
    private LocalDateTime dataAcao = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEntidade() { return entidade; }
    public void setEntidade(String entidade) { this.entidade = entidade; }
    public Long getEntidadeId() { return entidadeId; }
    public void setEntidadeId(Long entidadeId) { this.entidadeId = entidadeId; }
    public String getAcao() { return acao; }
    public void setAcao(String acao) { this.acao = acao; }
    public String getDadosAnteriores() { return dadosAnteriores; }
    public void setDadosAnteriores(String dadosAnteriores) { this.dadosAnteriores = dadosAnteriores; }
    public String getDadosNovos() { return dadosNovos; }
    public void setDadosNovos(String dadosNovos) { this.dadosNovos = dadosNovos; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public LocalDateTime getDataAcao() { return dataAcao; }
    public void setDataAcao(LocalDateTime dataAcao) { this.dataAcao = dataAcao; }
}
