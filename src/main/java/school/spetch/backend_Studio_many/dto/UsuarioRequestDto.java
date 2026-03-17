package school.spetch.backend_Studio_many.dto;

import java.time.LocalDateTime;

public class UsuarioRequestDto {
    private String nome;
    private String telefone;
    private String email;
    private String senha;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    public UsuarioRequestDto(){}

    public UsuarioRequestDto(String nome, String telefone, String email, String senha, LocalDateTime dataAtualizacao) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.senha = senha;
        this.dataCadastro = LocalDateTime.now();
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}
