package com.studio.core.dominio.cliente.dto;

import java.time.LocalDateTime;

public class ClienteResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private String endereco;
    private String observacoes;
    private String estagioFunil;
    private LocalDateTime dataCadastro;
    private Boolean ativo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public String getEstagioFunil() { return estagioFunil; }
    public void setEstagioFunil(String estagioFunil) { this.estagioFunil = estagioFunil; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public static ClienteResponseDTO fromEntity(com.studio.core.dominio.cliente.entity.Cliente entity) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setEmail(entity.getEmail());
        dto.setTelefone(entity.getTelefone());
        dto.setCpf(entity.getCpf());
        dto.setEndereco(entity.getEndereco());
        dto.setObservacoes(entity.getObservacoes());
        dto.setEstagioFunil(entity.getEstagioFunil());
        dto.setDataCadastro(entity.getDataCadastro());
        dto.setAtivo(entity.getAtivo());
        return dto;
    }
}
