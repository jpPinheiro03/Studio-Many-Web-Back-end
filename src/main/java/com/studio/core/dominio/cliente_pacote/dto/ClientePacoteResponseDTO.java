package com.studio.core.dominio.cliente_pacote.dto;

import com.studio.core.dominio.cliente.dto.ClienteResponseDTO;
import com.studio.core.dominio.pacote.dto.PacoteResponseDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClientePacoteResponseDTO {

    private Long id;
    private ClienteResponseDTO cliente;
    private PacoteResponseDTO pacote;
    private Integer sessoesRestantes;
    private String status;
    private LocalDateTime dataCompra;
    private LocalDate dataValidade;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ClienteResponseDTO getCliente() { return cliente; }
    public void setCliente(ClienteResponseDTO cliente) { this.cliente = cliente; }
    public PacoteResponseDTO getPacote() { return pacote; }
    public void setPacote(PacoteResponseDTO pacote) { this.pacote = pacote; }
    public Integer getSessoesRestantes() { return sessoesRestantes; }
    public void setSessoesRestantes(Integer sessoesRestantes) { this.sessoesRestantes = sessoesRestantes; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getDataCompra() { return dataCompra; }
    public void setDataCompra(LocalDateTime dataCompra) { this.dataCompra = dataCompra; }
    public LocalDate getDataValidade() { return dataValidade; }
    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }
}
