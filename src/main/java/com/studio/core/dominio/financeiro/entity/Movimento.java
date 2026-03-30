package com.studio.core.dominio.financeiro.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.usuario.entity.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity // Esta classe vira uma tabela no banco de dados
@Table(name = "movimentos") // Nome da tabela no banco (snake_case)
@Getter @Setter // Lombok: gera getters e setters automaticamente
public class Movimento {

    @Id // Chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento pelo banco
    private Long id;

    @Enumerated(EnumType.STRING) // Armazena o enum como texto no banco
    @Column(nullable = false) // Coluna obrigatória (NOT NULL)
    private TipoMovimento tipo;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos movimentos apontam para 1 agendamento (carregamento sob demanda)
    @JoinColumn(name = "agendamento_id") // Nome da coluna FK
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private Agendamento agendamento;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos movimentos apontam para 1 usuário (carregamento sob demanda)
    @JoinColumn(name = "usuario_id") // Nome da coluna FK
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private Usuario usuario;

    @Column(precision = 10, scale = 2, nullable = false) // Número decimal obrigatório (DECIMAL(10,2))
    private BigDecimal valor;

    @Column(name = "data_movimento", nullable = false) // Coluna obrigatória com nome customizado
    private LocalDate dataMovimento;

    @Column(columnDefinition = "TEXT") // Texto longo (sem limite de caracteres)
    private String descricao;

    @Column(name = "data_cadastro") // Nome da coluna no banco (snake_case)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @Transient // Campo não persistido no banco (só existe em memória)
    private Long agendamentoId;

    @Transient // Campo não persistido no banco (só existe em memória)
    private Long usuarioId;

    public enum TipoMovimento {
        RECEITA,
        DESPESA
    }
}
