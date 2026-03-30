package com.studio.core.dominio.auditoria.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.usuario.entity.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity // Esta classe vira uma tabela no banco de dados
@Table(name = "auditoria") // Nome da tabela no banco (snake_case)
@Getter @Setter // Lombok: gera getters e setters automaticamente
public class Auditoria {

    @Id // Chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento pelo banco
    private Long id;

    @Column(name = "entidade", nullable = false) // Coluna obrigatória com nome customizado
    private String entidade;

    @Column(name = "entidade_id", nullable = false) // Coluna obrigatória com nome customizado
    private Long entidadeId;

    @Column(name = "operacao", nullable = false) // Coluna obrigatória (NOT NULL)
    private String acao;

    @Column(columnDefinition = "TEXT") // Texto longo (sem limite de caracteres)
    private String dadosAnteriores;

    @Column(columnDefinition = "TEXT") // Texto longo (sem limite de caracteres)
    private String dadosNovos;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos registros de auditoria apontam para 1 usuário (carregamento sob demanda)
    @JoinColumn(name = "usuario_id") // Nome da coluna FK
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private Usuario usuario;

    @Column(name = "created_at", nullable = false) // Coluna obrigatória com nome customizado
    private LocalDateTime dataAcao = LocalDateTime.now();

    @Transient // Campo não persistido no banco (só existe em memória)
    private Long usuarioId;
}
