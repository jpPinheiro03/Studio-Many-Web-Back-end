package com.studio.core.dominio.horario_trabalho.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "horario_trabalho")
public class HorarioTrabalho {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Funcionario func;
    
    @Column(name = "dia_semana", nullable = false)
    private Integer diaSemana;
    
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;
    
    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;
    
    @Transient
    private Long funcId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Funcionario getFunc() { return func; }
    public void setFunc(Funcionario func) { this.func = func; }
    public Long getFuncionarioId() { return func != null ? func.getId() : funcId; }
    public void setFuncionarioId(Long funcId) { this.funcId = funcId; }
    public Integer getDiaSemana() { return diaSemana; }
    public void setDiaSemana(Integer diaSemana) { this.diaSemana = diaSemana; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFim() { return horaFim; }
    public void setHoraFim(LocalTime horaFim) { this.horaFim = horaFim; }
}
