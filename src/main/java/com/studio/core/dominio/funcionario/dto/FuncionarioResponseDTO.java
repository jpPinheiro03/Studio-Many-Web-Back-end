package com.studio.core.dominio.funcionario.dto;

import java.time.LocalDateTime;

public class FuncionarioResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private String especialidade;
    private Boolean ativo;
    private LocalDateTime dataCadastro;

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
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public static FuncionarioResponseDTO fromEntity(com.studio.core.dominio.funcionario.entity.Funcionario entity) {
        FuncionarioResponseDTO dto = new FuncionarioResponseDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setEmail(entity.getEmail());
        dto.setTelefone(entity.getTelefone());
        dto.setCpf(entity.getCpf());
        dto.setEspecialidade(entity.getEspecialidade());
        dto.setAtivo(entity.getAtivo());
        dto.setDataCadastro(entity.getDataCadastro());
        return dto;
    }
}
