package com.studio.core.dominio.horario_trabalho.controller;

import com.studio.core.dominio.horario_trabalho.dto.HorarioTrabalhoRequestDTO;
import com.studio.core.dominio.horario_trabalho.dto.HorarioTrabalhoResponseDTO;
import com.studio.core.service.HorarioTrabalhoService;
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
@RequestMapping("/api/horario-trabalho")
@Tag(name = "Horário de Trabalho", description = "Endpoints para gestão de horários de trabalho")
public class HorarioTrabalhoController {

    @Autowired
    private HorarioTrabalhoService service;

    @GetMapping
    @Operation(summary = "Listar todos os horários de trabalho")
    @ApiResponses(
            @ApiResponse(responseCode = "200",
                    description = "Horários encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                [
                                    {
                                        "id": 1,
                                        "func": {
                                            "id": 1,
                                            "nome": "Anny Beatriz",
                                            "email": "anny@gmail.com",
                                            "telefone": "1193742-1983",
                                            "cpf": "12345678912",
                                            "especialidade": "Depilação a Laser",
                                            "ativo": true,
                                            "dataCadastro": "2026-01-01T08:00:00.000Z"
                                        },
                                        "diaSemana": 2,
                                        "horaInicio": "08:00",
                                        "horaFim": "12:00"
                                    }
                                ]
                            """)
                    )
            )
    )
    public ResponseEntity<List<HorarioTrabalhoResponseDTO>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/funcionario/{id}")
    @Operation(summary = "Listar horário do funcionário por ID")
    @ApiResponses(
            @ApiResponse(responseCode = "200",
                    description = "Horário encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                [
                                    {
                                        "id": 1,
                                        "func": {
                                            "id": 1,
                                            "nome": "Anny Beatriz",
                                            "email": "anny@gmail.com",
                                            "telefone": "1193742-1983",
                                            "cpf": "12345678912",
                                            "especialidade": "Depilação a Laser",
                                            "ativo": true,
                                            "dataCadastro": "2026-01-01T08:00:00.000Z"
                                        },
                                        "diaSemana": 2,
                                        "horaInicio": "08:00",
                                        "horaFim": "12:00"
                                    }
                                ]
                            """)
                    )
            )
    )
    public ResponseEntity<List<HorarioTrabalhoResponseDTO>> porFuncionario(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByFuncionarioId(id));
    }

    @PostMapping
    @Operation(summary = "Criar horário de trabalho")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "201",
                            description = "Criado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = """
                                {
                                    "id": 2,
                                    "func": {
                                        "id": 1,
                                        "nome": "Anny Beatriz",
                                        "email": "anny@gmail.com",
                                        "telefone": "1193742-1983",
                                        "cpf": "12345678912",
                                        "especialidade": "Depilação a Laser",
                                        "ativo": true,
                                        "dataCadastro": "2026-01-01T08:00:00.000Z"
                                    },
                                    "diaSemana": 3,
                                    "horaInicio": "08:00",
                                    "horaFim": "12:00"
                                }
                            """)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Requisição inválida",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = """
                                {
                                    "status": 400,
                                    "error": "BAD REQUEST",
                                    "message": "Erro na validação dos campos",
                                    "errors": [
                                        "diaSemana: Dia da semana é obrigatório",
                                        "horaInicio: Hora de início é obrigatória",
                                        "horaFim: Hora de fim é obrigatória",
                                        "funcionarioId: Funcionário ID é obrigatório"
                                    ]
                                }
                            """)
                            )
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Funcionário não encontrado",
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
                    )
            }
    )
    public ResponseEntity<HorarioTrabalhoResponseDTO> criar(@Valid @RequestBody HorarioTrabalhoRequestDTO dto) {
        HorarioTrabalhoResponseDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir horário por ID")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "204",
                            description = "Horário excluído"
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "ID não encontrado"
                    )
            }
    )
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}