package com.studio.core.dominio.lista_espera.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.cliente.entity.Cliente;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import com.studio.core.dominio.servico.entity.Servico;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity // Esta classe vira uma tabela no banco de dados
@Table(name = "lista_espera") // Nome da tabela no banco (snake_case)
@Getter @Setter // Lombok: gera getters e setters automaticamente
public class ListaEspera {

    @Id // Chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento pelo banco
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos registros apontam para 1 cliente (carregamento sob demanda)
    @JoinColumn(name = "cliente_id", nullable = false) // Nome da coluna FK obrigatória
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos registros apontam para 1 serviço (carregamento sob demanda)
    @JoinColumn(name = "servico_id", nullable = false) // Nome da coluna FK obrigatória
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private Servico servico;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos registros apontam para 1 funcionário (carregamento sob demanda)
    @JoinColumn(name = "funcionario_id") // Nome da coluna FK (opcional)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private Funcionario funcionario;

    @Column(name = "data_desejada") // Nome da coluna no banco (snake_case)
    private LocalDate dataDesejada;

    @Column(name = "horario_desejado") // Nome da coluna no banco (snake_case)
    private LocalTime horarioDesejado;

    @Enumerated(EnumType.STRING) // Armazena o enum como texto no banco
    private StatusListaEspera status = StatusListaEspera.AGUARDANDO;

    private String observacoes; // Coluna opcional (pode ser nula)

    @Column(name = "data_cadastro") // Nome da coluna no banco (snake_case)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @Transient // Campo não persistido no banco (só existe em memória)
    private Long clienteId;

    @Transient // Campo não persistido no banco (só existe em memória)
    private Long servicoId;

    @Transient // Campo não persistido no banco (só existe em memória)
    private Long funcionarioId;

    public enum StatusListaEspera {
        AGUARDANDO,
        ATENDIDO,
        CANCELADO
    }
}
