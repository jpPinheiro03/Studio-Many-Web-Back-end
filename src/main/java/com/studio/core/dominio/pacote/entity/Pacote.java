package com.studio.core.dominio.pacote.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.servico.entity.Servico;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity // Esta classe vira uma tabela no banco de dados
@Table(name = "pacotes") // Nome da tabela no banco (snake_case)
@Getter @Setter // Lombok: gera getters e setters automaticamente
public class Pacote {

    @Id // Chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento pelo banco
    private Long id;

    @Column(nullable = false) // Coluna obrigatória (NOT NULL)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos pacotes apontam para 1 serviço (carregamento sob demanda)
    @JoinColumn(name = "servico_id", nullable = false) // Nome da coluna FK obrigatória
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private Servico servico;

    @Column(name = "quantidade_sessoes", nullable = false) // Coluna obrigatória com nome customizado
    private Integer quantidadeSessoes;

    @Column(precision = 10, scale = 2, nullable = false) // Número decimal obrigatório (DECIMAL(10,2))
    private BigDecimal preco;

    @Column(name = "validade_dias") // Nome da coluna no banco (snake_case)
    private Integer validadeDias;

    private Boolean ativo = true; // Coluna opcional com valor padrão

    @Column(name = "data_cadastro") // Nome da coluna no banco (snake_case)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @Transient // Campo não persistido no banco (só existe em memória)
    private Long servicoId;
}
