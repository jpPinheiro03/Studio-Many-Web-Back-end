package com.studio.core.dominio.cliente_pacote.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.cliente.entity.Cliente;
import com.studio.core.dominio.pacote.entity.Pacote;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity // Esta classe vira uma tabela no banco de dados
@Table(name = "cliente_pacotes", uniqueConstraints = { // Nome da tabela no banco com restrição de unicidade
    @UniqueConstraint(columnNames = {"cliente_id", "pacote_id"}) // Impede combinação duplicada de cliente e pacote
})
@Getter @Setter // Lombok: gera getters e setters automaticamente
public class ClientePacote {

    @Id // Chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento pelo banco
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos registros apontam para 1 cliente (carregamento sob demanda)
    @JoinColumn(name = "cliente_id", nullable = false) // Nome da coluna FK obrigatória
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY) // Muitos registros apontam para 1 pacote (carregamento sob demanda)
    @JoinColumn(name = "pacote_id", nullable = false) // Nome da coluna FK obrigatória
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Evita erro de serialização JSON no lazy loading
    private Pacote pacote;

    @Column(name = "sessoes_usadas", nullable = false) // Coluna obrigatória com nome customizado
    private Integer sessoesUsadas = 0;

    @Column(name = "data_validade") // Nome da coluna no banco (snake_case)
    private LocalDate dataValidade;

    @Column(name = "data_compra") // Nome da coluna no banco (snake_case)
    private LocalDateTime dataCompra = LocalDateTime.now();

    @Enumerated(EnumType.STRING) // Armazena o enum como texto no banco
    private StatusClientePacote status = StatusClientePacote.ATIVO;

    public enum StatusClientePacote {
        ATIVO,
        EXPIRADO,
        USADO
    }
}
