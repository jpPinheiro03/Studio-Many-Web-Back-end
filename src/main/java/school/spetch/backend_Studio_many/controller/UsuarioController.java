package school.spetch.backend_Studio_many.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.spetch.backend_Studio_many.dto.UsuarioMapper;
import school.spetch.backend_Studio_many.dto.UsuarioRequestDto;
import school.spetch.backend_Studio_many.dto.UsuarioResponseDto;
import school.spetch.backend_Studio_many.entity.Usuario;
import school.spetch.backend_Studio_many.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @Operation(summary = "Cadastrar usuário")
    @ApiResponses({

            @ApiResponse(
                    responseCode = "201",
                    description = "Criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                            {
                "id": 1,
                "nome": "Isabelly",
                "telefone": "11961969921",
                "email": "isa@email.com",
                "senha": "123",
                "dataCadastro": "2026-03-22T17:30:00.687214468",
                "dataAtualizacao": null
                                    }
            """)
                    )
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
            {
            "status": 400,
            "error": "BAD REQUEST",
            "message": "Erro na validação dos campos",
            "errors": [
            "senha: A senha não pode ser nulo ou vazio",
            "email: O email não pode ser nulo ou vazio",
            "telefone: O telefone deve haver entre 7 e 15 caracteres",
            "nome: O nome não pode ser nulo ou vazio",
            "telefone: O telefone não pode ser nulo ou vazio"
                ]
            }
            """)
                    )
            ),

            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
            {
              "status": 409,
              "error": "Conflict",
              "message": "Já existe um usuário com este email"
            }
            """)
                    )
            )
    })
    public ResponseEntity<UsuarioResponseDto> cadastrar(@RequestBody @Valid UsuarioRequestDto dto){
        Usuario u = usuarioService.cadastrar(UsuarioMapper.toModel(dto));

        UsuarioResponseDto responseDto = UsuarioMapper.toResponseDto(u);

        return ResponseEntity.status(201).body(responseDto);
    }
}
