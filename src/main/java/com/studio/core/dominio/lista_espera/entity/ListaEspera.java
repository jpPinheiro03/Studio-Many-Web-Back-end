package com.studio.core.dominio.lista_espera.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.cliente.entity.Cliente;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.dominio.servico.entity.Servico;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "lista_espera")
public class ListaEspera {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servico_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Servico servico;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Funcionario func;
    
    @Column(name = "data_desejada")
    private LocalDate dataDesejada;
    
    @Column(name = "horario_desejado")
    private LocalTime horarioDesejado;
    
    @Enumerated(EnumType.STRING)
    private StatusListaEspera status = StatusListaEspera.AGUARDANDO;
    
    private String observacoes;
    
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro = LocalDateTime.now();
    
    @Transient
    private Long clienteId;
    
    @Transient
    private Long servicoId;
    
    @Transient
    private Long funcId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Long getClienteId() { return cliente != null ? cliente.getId() : clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public Servico getServico() { return servico; }
    public void setServico(Servico servico) { this.servico = servico; }
    public Long getServicoId() { return servico != null ? servico.getId() : servicoId; }
    public void setServicoId(Long servicoId) { this.servicoId = servicoId; }
    public Funcionario getFunc() { return func; }
    public void setFunc(Funcionario func) { this.func = func; }
    public Long getFuncionarioId() { return func != null ? func.getId() : funcId; }
    public void setFuncionarioId(Long funcId) { this.funcId = funcId; }
    public LocalDate getDataDesejada() { return dataDesejada; }
    public void setDataDesejada(LocalDate dataDesejada) { this.dataDesejada = dataDesejada; }
    public LocalTime getHorarioDesejado() { return horarioDesejado; }
    public void setHorarioDesejado(LocalTime horarioDesejado) { this.horarioDesejado = horarioDesejado; }
    public StatusListaEspera getStatus() { return status; }
    public void setStatus(StatusListaEspera status) { this.status = status; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public enum StatusListaEspera {
        AGUARDANDO,
        ATENDIDO,
        CANCELADO
    }
}
