package com.studio.core.dominio.bloqueio.controller;

import com.studio.core.dominio.bloqueio.dto.BloqueioRequestDTO;
import com.studio.core.dominio.bloqueio.dto.BloqueioResponseDTO;
import com.studio.core.service.BloqueioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bloqueios")
@Tag(name = "Bloqueios", description = "Endpoints para gestão de bloqueios de funcionários")
public class BloqueioController {
    
    @Autowired
    private BloqueioService service;
    
    @GetMapping
    @Operation(summary = "Listar usuários bloqueados")
    @ApiResponse(
            responseCode = "200",
            description = "Retorna os usuário bloqueado",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            [
                                              {
                                                "id": 1,
                                                "funcionario": {
                                                  "id": 1,
                                                  "nome": "Mariana",
                                                  "email": "mariana@gmail.com",
                                                  "telefone": "193849308",
                                                  "cpf": "483929402",
                                                  "especialidade": "Cabeleleira",
                                                  "ativo": true,
                                                  "dataCadastro": "2026-03-31T02:47:23.606Z"
                                                },
                                                "dataInicio": "2026-03-31T02:47:23.606Z",
                                                "dataFim": "2026-03-31T02:47:23.606Z",
                                                "motivo": "Desligamento temporário"
                                              },
                                              {
                                                "id": 2,
                                                "funcionario": {
                                                  "id": 1,
                                                  "nome": "Rochele",
                                                  "email": "rochele@gmail.com",
                                                  "telefone": "591843368",
                                                  "cpf": "683323410",
                                                  "especialidade": "Unha",
                                                  "ativo": true,
                                                  "dataCadastro": "2026-03-31T02:47:23.606Z"
                                                },
                                                "dataInicio": "2026-03-31T02:47:23.606Z",
                                                "dataFim": "2026-03-31T02:47:23.606Z",
                                                "motivo": "Desligamento temporário"
                                              }
                                            ]
            """)
            )
    )
    public ResponseEntity<List<BloqueioResponseDTO>> listar() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/funcionario/{id}")
    @Operation(summary = "Exibe um usuário bloqueado")
    @ApiResponse(
            responseCode = "200",
            description = "Retorna o usuário bloqueado",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            [
                                              {
                                                "id": 1,
                                                "funcionario": {
                                                  "id": 1,
                                                  "nome": "Mariana",
                                                  "email": "mariana@gmail.com",
                                                  "telefone": "193849308",
                                                  "cpf": "483929402",
                                                  "especialidade": "Cabeleleira",
                                                  "ativo": true,
                                                  "dataCadastro": "2026-03-31T02:47:23.606Z"
                                                },
                                                "dataInicio": "2026-03-31T02:47:23.606Z",
                                                "dataFim": "2026-03-31T02:47:23.606Z",
                                                "motivo": "Desligamento temporário"
                                              }
                                            ]
            """)
            )
    )
    public ResponseEntity<List<BloqueioResponseDTO>> porFuncionario(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByFuncionarioId(id));
    }
    
    @PostMapping
    @Operation(summary = "Bloquear usuário")
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário bloqueado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                            {
                "id": 1,
                "nome": "Isabelly",
                "dataInicio": "2026-03-29T20:36:32.949Z",
                "dataFim": "2026-04-29T20:36:32.949Z",
                "Motivo": "Férias"
                                    }
            """)
                    )
            ),
            @ApiResponse(
            responseCode = "404",
            description = "Recurso não encontrado",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
            {
            "status": 404,
            "error": "NOT FOUND",
            "message": "Funcionário não encontrado"
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
            "message": "Data de início deve ser anterior à data de fim"
            }
            """)
                    )
            )
    })
    public ResponseEntity<BloqueioResponseDTO> criar(@Valid @RequestBody BloqueioRequestDTO dto) {
        BloqueioResponseDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    @ApiResponses({

            @ApiResponse(
                    responseCode = "204",
                    description = "Bloqueio é excluído",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                            {}
            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                            {
            "status": 404,
            "error": "NOT FOUND",
            "message": "Bloqueio não encontrado"
            }
            """)
                    )
            )
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
