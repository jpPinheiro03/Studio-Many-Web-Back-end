package school.spetch.backend_Studio_many.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class UsuarioRequestDto {
    @NotBlank(message = "O nome não pode ser nulo ou vazio")
    @Schema(example = "Isabelly")
    private String nome;

    @NotBlank(message = "O telefone não pode ser nulo ou vazio")
    @Size(min = 7, max = 15, message = "O telefone deve haver entre 7 e 15 caracteres")
    @Schema(example = "11961969921")
    private String telefone;

    @NotBlank(message = "O email não pode ser nulo ou vazio")
    @Email(message = "O email deve conter '@'")
    @Schema(example = "isabelly@gmail.com")
    private String email;

    @NotBlank(message = "A senha não pode ser nulo ou vazio")
    @Schema(example = "123123")
    private String senha;

    @Schema(example = "2026-03-17T19:05:32")
    private LocalDateTime dataCadastro;

    @Nullable
    @Schema(nullable = true)
    private LocalDateTime dataAtualizacao;

    public UsuarioRequestDto(){}

    public UsuarioRequestDto(String nome, String telefone, String email, String senha, LocalDateTime dataCadastro, LocalDateTime dataAtualizacao) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.senha = senha;
        this.dataCadastro = dataCadastro;
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
