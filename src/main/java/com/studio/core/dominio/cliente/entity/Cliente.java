package com.studio.core.dominio.cliente.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity // Esta classe vira uma tabela no banco de dados
@Table(name = "clientes") // Nome da tabela no banco (snake_case)
@Getter @Setter // Lombok: gera getters e setters automaticamente
public class Cliente {

    @Id // Chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento pelo banco
    private Long id;

    @Column(nullable = false) // Coluna obrigatória (NOT NULL)
    private String nome;

    @Column(unique = true) // Valor único na tabela (não pode repetir)
    private String email;

    @Column(nullable = false) // Coluna obrigatória (NOT NULL)
    private String telefone;

    @Column(unique = true) // Valor único na tabela (não pode repetir)
    private String cpf;

    private String endereco; // Coluna opcional (pode ser nula)

    private String observacoes; // Coluna opcional (pode ser nula)

    @Column(name = "estagio_funil") // Nome da coluna no banco (snake_case)
    private String estagioFunil = "NOVO";

    @Column(name = "data_cadastro") // Nome da coluna no banco (snake_case)
    private LocalDateTime dataCadastro = LocalDateTime.now();

    private Boolean ativo = true; // Coluna opcional com valor padrão
}
