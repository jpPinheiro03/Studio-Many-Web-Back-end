package com.studio.core.dominio.cliente_pacote.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.studio.core.dominio.cliente.entity.Cliente;
import com.studio.core.dominio.pacote.entity.Pacote;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cliente_pacotes")
public class ClientePacote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pacote_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Pacote pacote;
    
    @Column(name = "sessoes_usadas", nullable = false)
    private Integer sessoesUsadas = 0;
    
    @Column(name = "data_validade")
    private LocalDate dataValidade;
    
    @Column(name = "data_compra")
    private LocalDateTime dataCompra = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    private StatusClientePacote status = StatusClientePacote.ATIVO;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Pacote getPacote() { return pacote; }
    public void setPacote(Pacote pacote) { this.pacote = pacote; }
    public Integer getSessoesUsadas() { return sessoesUsadas; }
    public void setSessoesUsadas(Integer sessoesUsadas) { this.sessoesUsadas = sessoesUsadas; }
    public LocalDate getDataValidade() { return dataValidade; }
    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }
    public LocalDateTime getDataCompra() { return dataCompra; }
    public void setDataCompra(LocalDateTime dataCompra) { this.dataCompra = dataCompra; }
    public StatusClientePacote getStatus() { return status; }
    public void setStatus(StatusClientePacote status) { this.status = status; }

    public enum StatusClientePacote {
        ATIVO,
        EXPIRADO,
        USADO
    }
}
