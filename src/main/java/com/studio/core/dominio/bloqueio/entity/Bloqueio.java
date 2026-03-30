package com.studio.core.dominio.bloqueio.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity // Esta classe vira uma tabela no banco de dados
@Table(name = "bloqueios") // Nome da tabela no banco (snake_case)
@Getter @Setter // Lombok: gera getters e setters automaticamente
public class Bloqueio {

    @Id // Chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento pelo banco
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos bloqueios apontam para 1 funcionário (carregamento sob demanda)
    @JoinColumn(name = "funcionario_id", nullable = false) // Nome da coluna FK obrigatória
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private Funcionario funcionario;

    @Column(name = "data_inicio", nullable = false) // Coluna obrigatória com nome customizado
    private LocalDateTime dataInicio;

    @Column(name = "data_fim", nullable = false) // Coluna obrigatória com nome customizado
    private LocalDateTime dataFim;

    private String motivo; // Coluna opcional (pode ser nula)

    @Transient // Campo não persistido no banco (só existe em memória)
    private Long funcionarioId;
}
