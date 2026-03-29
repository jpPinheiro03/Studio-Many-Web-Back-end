package com.studio.core.dominio.usuario.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.funcionario.entity.Funcionario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity // Esta classe vira uma tabela no banco de dados
@Table(name = "usuarios") // Nome da tabela no banco (snake_case)
@Getter @Setter // Lombok: gera getters e setters automaticamente
public class Usuario {

    @Id // Chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento pelo banco
    private Long id;

    @Column(unique = true, nullable = false) // Valor único e obrigatório
    private String email;

    @Column(nullable = false) // Coluna obrigatória (NOT NULL)
    private String senha;

    @Enumerated(EnumType.STRING) // Armazena o enum como texto no banco
    @Column(nullable = false) // Coluna obrigatória (NOT NULL)
    private Role role = Role.FUNCIONARIO;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos usuários apontam para 1 funcionário (carregamento sob demanda)
    @JoinColumn(name = "funcionario_id") // Nome da coluna FK
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private Funcionario funcionario;

    @Column(name = "data_cadastro") // Nome da coluna no banco (snake_case)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    private Boolean ativo = true; // Coluna opcional com valor padrão

    public enum Role {
        ADMIN,
        FUNCIONARIO
    }
}
