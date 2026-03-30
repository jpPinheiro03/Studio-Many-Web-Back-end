package com.studio.core.exception;

import com.studio.core.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@RestControllerAdvice // Trata exceções de todos os controllers (intercepta erros globalmente)
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class) // Captura exceções ResourceNotFoundException e retorna resposta personalizada
    public ResponseEntity<ApiResponse<Void>> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(404, ex.getMessage()));
    }
    
    @ExceptionHandler(BadRequestException.class) // Captura exceções BadRequestException e retorna resposta personalizada
    public ResponseEntity<ApiResponse<Void>> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(400, ex.getMessage()));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class) // Captura erros de validação de @Valid e retorna resposta personalizada
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
            .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(400, message));
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class) // Captura erros de JSON malformado e retorna resposta personalizada
    public ResponseEntity<ApiResponse<Void>> handleUnreadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(400, "Corpo da requisição inválido ou malformado"));
    }
    
    @ExceptionHandler(MethodArgumentTypeMismatchException.class) // Captura erros de tipo de parâmetro inválido e retorna resposta personalizada
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = "Parâmetro '" + ex.getName() + "' com valor inválido: " + ex.getValue();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error(400, message));
    }
    
    @ExceptionHandler(AccessDeniedException.class) // Captura exceções de acesso negado (403) e retorna resposta personalizada
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ApiResponse.error(403, "Acesso negado. Você não tem permissão para esta ação."));
    }
    
    @ExceptionHandler(BadCredentialsException.class) // Captura exceções de credenciais inválidas (401) e retorna resposta personalizada
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ApiResponse.error(401, "Credenciais inválidas"));
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class) // Captura exceções de método HTTP não suportado (405) e retorna resposta personalizada
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(ApiResponse.error(405, "Método HTTP não suportado: " + ex.getMethod()));
    }
    
    @ExceptionHandler(Exception.class) // Captura qualquer exceção não tratada e retorna erro genérico (500)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(500, "Erro interno do servidor"));
    }
}
