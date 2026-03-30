package com.studio.core.dominio.horario_trabalho.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Entity // Esta classe vira uma tabela no banco de dados
@Table(name = "horario_trabalho", uniqueConstraints = { // Nome da tabela no banco com restrição de unicidade
    @UniqueConstraint(columnNames = {"funcionario_id", "dia_semana", "hora_inicio"}) // Impede horários duplicados para o mesmo funcionário no mesmo dia
})
@Getter @Setter // Lombok: gera getters e setters automaticamente
public class HorarioTrabalho {

    @Id // Chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento pelo banco
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos horários apontam para 1 funcionário (carregamento sob demanda)
    @JoinColumn(name = "funcionario_id", nullable = false) // Nome da coluna FK obrigatória
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private Funcionario funcionario;

    @Column(name = "dia_semana", nullable = false) // Coluna obrigatória com nome customizado
    private Integer diaSemana;

    @Column(name = "hora_inicio", nullable = false) // Coluna obrigatória com nome customizado
    private LocalTime horaInicio;

    @Column(name = "hora_fim", nullable = false) // Coluna obrigatória com nome customizado
    private LocalTime horaFim;

    @Transient // Campo não persistido no banco (só existe em memória)
    private Long funcionarioId;
}
