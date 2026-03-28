package com.studio.core.dominio.agendamento.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.cliente.entity.Cliente;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.dominio.servico.entity.Servico;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamentos")
public class Agendamento {
    
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
    @JoinColumn(name = "funcionario_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Funcionario funcionario;
    
    @Column(name = "data_hora_inicio", nullable = false)
    private LocalDateTime dataHoraInicio;
    
    @Column(name = "data_hora_fim", nullable = false)
    private LocalDateTime dataHoraFim;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private StatusAgendamento status = StatusAgendamento.PENDENTE;
    
    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;
    
    @Column(name = "valor_sinal", precision = 10, scale = 2)
    private BigDecimal valorSinal = BigDecimal.ZERO;
    
    @Column(name = "quantidade_parcelas")
    private Integer quantidadeParcelas = 1;
    
    @Column(name = "motivo_cancelamento", columnDefinition = "TEXT")
    private String motivoCancelamento;
    
    private String observacoes;
    
    @Transient
    private Long clienteId;
    
    @Transient
    private Long servicoId;
    
    @Transient
    private Long funcionarioId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Long getClienteId() { return cliente != null ? cliente.getId() : clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public Long getServicoId() { return servico != null ? servico.getId() : servicoId; }
    public void setServicoId(Long servicoId) { this.servicoId = servicoId; }
    public Long getFuncionarioId() { return funcionario != null ? funcionario.getId() : funcionarioId; }
    public void setFuncionarioId(Long funcId) { this.funcionarioId = funcId; }
    public Servico getServico() { return servico; }
    public void setServico(Servico servico) { this.servico = servico; }
    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }
    public LocalDateTime getDataHoraInicio() { return dataHoraInicio; }
    public void setDataHoraInicio(LocalDateTime dataHoraInicio) { this.dataHoraInicio = dataHoraInicio; }
    public LocalDateTime getDataHoraFim() { return dataHoraFim; }
    public void setDataHoraFim(LocalDateTime dataHoraFim) { this.dataHoraFim = dataHoraFim; }
    public StatusAgendamento getStatus() { return status; }
    public void setStatus(StatusAgendamento status) { this.status = status; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public BigDecimal getValorSinal() { return valorSinal; }
    public void setValorSinal(BigDecimal valorSinal) { this.valorSinal = valorSinal; }
    public Integer getQuantidadeParcelas() { return quantidadeParcelas; }
    public void setQuantidadeParcelas(Integer quantidadeParcelas) { this.quantidadeParcelas = quantidadeParcelas; }
    public String getMotivoCancelamento() { return motivoCancelamento; }
    public void setMotivoCancelamento(String motivoCancelamento) { this.motivoCancelamento = motivoCancelamento; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
}
