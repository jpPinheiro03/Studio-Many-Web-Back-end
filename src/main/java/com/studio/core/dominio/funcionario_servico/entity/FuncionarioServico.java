package com.studio.core.dominio.funcionario_servico.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.dominio.servico.entity.Servico;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "funcionario_servicos")
public class FuncionarioServico {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Funcionario func;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servico_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Servico servico;
    
    @Column(name = "percentual_comissao", precision = 5, scale = 2)
    private BigDecimal percentualComissao;
    
    @Transient
    private Long funcId;
    
    @Transient
    private Long servicoId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Funcionario getFunc() { return func; }
    public void setFunc(Funcionario func) { this.func = func; }
    public Long getFuncionarioId() { return func != null ? func.getId() : funcId; }
    public void setFuncionarioId(Long funcId) { this.funcId = funcId; }
    public Servico getServico() { return servico; }
    public void setServico(Servico servico) { this.servico = servico; }
    public Long getServicoId() { return servico != null ? servico.getId() : servicoId; }
    public void setServicoId(Long servicoId) { this.servicoId = servicoId; }
    public BigDecimal getPercentualComissao() { return percentualComissao; }
    public void setPercentualComissao(BigDecimal percentualComissao) { this.percentualComissao = percentualComissao; }
}
