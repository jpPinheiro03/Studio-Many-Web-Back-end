package com.studio.core.dominio.agendamento.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.cliente.entity.Cliente;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.dominio.servico.entity.Servico;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity // Esta classe vira uma tabela no banco de dados
@Table(name = "agendamentos") // Nome da tabela no banco (snake_case)
@Getter @Setter // Lombok: gera getters e setters automaticamente
public class Agendamento {

    @Id // Chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento pelo banco
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos agendamentos apontam para 1 cliente (carregamento sob demanda)
    @JoinColumn(name = "cliente_id", nullable = false) // Nome da coluna FK obrigatória
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos agendamentos apontam para 1 serviço (carregamento sob demanda)
    @JoinColumn(name = "servico_id", nullable = false) // Nome da coluna FK obrigatória
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private Servico servico;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos agendamentos apontam para 1 funcionário (carregamento sob demanda)
    @JoinColumn(name = "funcionario_id", nullable = false) // Nome da coluna FK obrigatória
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private Funcionario funcionario;

    @Column(name = "data_hora_inicio", nullable = false) // Coluna obrigatória com nome customizado
    private LocalDateTime dataHoraInicio;

    @Column(name = "data_hora_fim", nullable = false) // Coluna obrigatória com nome customizado
    private LocalDateTime dataHoraFim;

    @Enumerated(EnumType.STRING) // Armazena o enum como texto no banco
    @Column(length = 30) // Tamanho máximo da coluna (VARCHAR(30))
    private StatusAgendamento status = StatusAgendamento.PENDENTE;

    @Column(name = "valor_total", precision = 10, scale = 2) // Número decimal (DECIMAL(10,2))
    private BigDecimal valorTotal;

    @Column(name = "valor_sinal", precision = 10, scale = 2) // Número decimal (DECIMAL(10,2))
    private BigDecimal valorSinal = BigDecimal.ZERO;

    @Column(name = "quantidade_parcelas") // Nome da coluna no banco (snake_case)
    private Integer quantidadeParcelas = 1;

    @Column(name = "comprovante_sinal") // Nome da coluna no banco (snake_case)
    private String comprovanteSinal;

    @Column(name = "data_confirmacao_sinal") // Nome da coluna no banco (snake_case)
    private LocalDateTime dataConfirmacaoSinal;

    @Column(name = "data_chegada") // Nome da coluna no banco (snake_case)
    private LocalDateTime dataChegada;

    @Column(name = "data_finalizacao") // Nome da coluna no banco (snake_case)
    private LocalDateTime dataFinalizacao;

    @Column(name = "motivo_cancelamento", columnDefinition = "TEXT") // Texto longo com nome customizado
    private String motivoCancelamento;

    private String observacoes; // Coluna opcional (pode ser nula)

    @Column(name = "agendamento_original_id") // Nome da coluna no banco (snake_case)
    private Long agendamentoOriginalId;

    @Column(name = "quantidade_reagendamentos") // Nome da coluna no banco (snake_case)
    private Integer quantidadeReagendamentos = 0;

    @Column(name = "taxa_cancelamento", precision = 10, scale = 2) // Número decimal (DECIMAL(10,2))
    private BigDecimal taxaCancelamento;

    @Column(name = "data_criacao") // Nome da coluna no banco (snake_case)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "recorrente") // Nome da coluna no banco (snake_case)
    private Boolean recorrente = false;

    @Column(name = "frequencia_recorrencia") // Nome da coluna no banco (snake_case)
    private String frequenciaRecorrencia;

    @Transient // Campo não persistido no banco (só existe em memória)
    private Long clienteId;

    @Transient // Campo não persistido no banco (só existe em memória)
    private Long servicoId;

    @Transient // Campo não persistido no banco (só existe em memória)
    private Long funcionarioId;
}
