package school.spetch.backend_Studio_many.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntidadeExistenteException.class)
    public ResponseEntity<ErroResponse> handleConflitoUsuarioExistente(EntidadeExistenteException e){
        ErroResponse erro = new ErroResponse(409, "CONFLICT", e.getMessage());

        return ResponseEntity.status(409).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleBadRequest(MethodArgumentNotValidException e){
        List<String> erros = new ArrayList<>();

        for(FieldError error : e.getBindingResult().getFieldErrors()){
            erros.add(error.getField() + ": " + error.getDefaultMessage());
        }

        ErroResponse erroResponse = new ErroResponse(400, "BAD REQUEST", "Erro na validação dos campos", erros);

        return ResponseEntity.status(400).body(erroResponse);
    }
}
