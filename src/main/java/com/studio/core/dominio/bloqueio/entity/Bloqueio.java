package com.studio.core.dominio.bloqueio.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bloqueios")
public class Bloqueio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Funcionario func;
    
    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;
    
    @Column(name = "data_fim", nullable = false)
    private LocalDateTime dataFim;
    
    private String motivo;
    
    @Transient
    private Long funcId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Funcionario getFunc() { return func; }
    public void setFunc(Funcionario func) { this.func = func; }
    public Long getFuncionarioId() { return func != null ? func.getId() : funcId; }
    public void setFuncionarioId(Long funcId) { this.funcId = funcId; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }
    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
