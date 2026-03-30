package com.studio.core.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data //Serve para não precisar ficar gerando codigo comum toda hora como get e set o Lombok: gera getters, setters, toString, equals, hashCode. automaticamente Ou seja, elimina boilerplate.
/*
@Data gera equals e hashCode com todos os campos.

Isso pode causar problemas em:
entidades JPA
objetos com relações

Alternativa em projetos grandes:
@Getter
@Setter

em vez de @Data.
 */
@AllArgsConstructor // Lombok: gera construtor com todos os argumentos
@NoArgsConstructor // Lombok: gera construtor sem argumentos
@JsonInclude(JsonInclude.Include.NON_NULL) // Não inclui campos nulos no JSON de resposta
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Sucesso", data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(201, "Criado com sucesso", data, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(int status, String message) {
        return new ApiResponse<>(status, message, null, LocalDateTime.now());
    }
}
