package com.studio.core.dominio.funcionario_servico.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.dominio.servico.entity.Servico;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity // Esta classe vira uma tabela no banco de dados
@Table(name = "funcionario_servicos") // Nome da tabela no banco (snake_case)
@Getter @Setter // Lombok: gera getters e setters automaticamente
public class FuncionarioServico {

    @Id // Chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento pelo banco
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos registros apontam para 1 funcionário (carregamento sob demanda)
    @JoinColumn(name = "funcionario_id", nullable = false) // Nome da coluna FK obrigatória
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private Funcionario funcionario;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos registros apontam para 1 serviço (carregamento sob demanda)
    @JoinColumn(name = "servico_id", nullable = false) // Nome da coluna FK obrigatória
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private Servico servico;

    @Column(name = "percentual_comissao", precision = 5, scale = 2) // Número decimal (DECIMAL(5,2)) com nome customizado
    private BigDecimal percentualComissao;

    @Transient // Campo não persistido no banco (só existe em memória)
    private Long funcionarioId;

    @Transient // Campo não persistido no banco (só existe em memória)
    private Long servicoId;
}
