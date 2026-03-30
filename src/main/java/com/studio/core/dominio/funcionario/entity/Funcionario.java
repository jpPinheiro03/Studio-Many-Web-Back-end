package com.studio.core.dominio.funcionario.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity // Esta classe vira uma tabela no banco de dados
@Table(name = "funcionarios") // Nome da tabela no banco (snake_case)
@Getter @Setter // Lombok: gera getters e setters automaticamente
public class Funcionario {

    @Id // Chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento pelo banco
    private Long id;

    @Column(nullable = false) // Coluna obrigatória (NOT NULL)
    private String nome;

    @Column(unique = true, nullable = false) // Valor único e obrigatório
    private String email;

    @Column(nullable = false) // Coluna obrigatória (NOT NULL)
    private String telefone;

    private String cpf; // Coluna opcional (pode ser nula)

    private String especialidade; // Coluna opcional (pode ser nula)

    private Boolean ativo = true; // Coluna opcional com valor padrão

    @Column(name = "data_cadastro") // Nome da coluna no banco (snake_case)
    private LocalDateTime dataCadastro = LocalDateTime.now();
}
