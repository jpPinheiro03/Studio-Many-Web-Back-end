package com.studio.core.dominio.pacote.controller;

import com.studio.core.dominio.pacote.dto.PacoteRequestDTO;
import com.studio.core.dominio.pacote.dto.PacoteResponseDTO;
import com.studio.core.dominio.pacote.entity.Pacote;
import com.studio.core.dominio.pacote.mapper.PacoteMapper;
import com.studio.core.service.PacoteService;
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
@RequestMapping("/api/pacotes")
@Tag(name = "Pacotes", description = "Endpoints para gestão de pacotes")
public class PacoteController {
    
    @Autowired
    private PacoteService service;
    
    @Autowired
    private PacoteMapper pacoteMapper;
    
    @GetMapping
    @Operation(summary = "Listar pacotes")
    @ApiResponses(
            @ApiResponse(responseCode = "200",
                    description = "Pacotes encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                [
                                    {
                                        "id": 1,
                                        "nome": "Depilação a laser 5x",
                                        "servico": {
                                            "id": 1,
                                            "nome": "Depilação a Laser",
                                            "descricao": "Depilação a Laser",
                                            "preco": 100,
                                            "duracaoMinutos": 30,
                                            "ativo": true
                                        },
                                        "quantidadeSessoes": 5,
                                        "preco": 110,
                                        "validadeDias": 90,
                                        "ativo": true,
                                        "dataCadastro": "2026-03-28T20:59:47.350Z"
                                    }
                                ]
                            """)
                    )
            )
    )
    public ResponseEntity<List<PacoteResponseDTO>> listar() {
        List<PacoteResponseDTO> dtos = service.findAll().stream()
            .map(pacoteMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Pacote encontrado por ID")
    @ApiResponses(
            @ApiResponse(responseCode = "200",
                    description = "Pacote encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "id": 1,
                                    "nome": "Depilação a laser 5x",
                                    "servico": {
                                        "id": 1,
                                        "nome": "Depilação a Laser",
                                        "descricao": "Depilação a Laser",
                                        "preco": 100,
                                        "duracaoMinutos": 30,
                                        "ativo": true
                                    },
                                    "quantidadeSessoes": 5,
                                    "preco": 110,
                                    "validadeDias": 90,
                                    "ativo": true,
                                    "dataCadastro": "2026-03-28T20:59:47.350Z"
                                }
                            """)
                    )
            )
    )
    public ResponseEntity<PacoteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pacoteMapper.toResponse(service.findById(id)));
    }
    
    @PostMapping
    @Operation(summary = "Criar pacote")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Pacote cadastrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "id": 1,
                                    "nome": "Depilação a laser 5x",
                                    "servico": {
                                        "id": 1,
                                        "nome": "Depilação a Laser",
                                        "descricao": "Depilação a Laser",
                                        "preco": 100,
                                        "duracaoMinutos": 30,
                                        "ativo": true
                                    },
                                    "quantidadeSessoes": 5,
                                    "preco": 110,
                                    "validadeDias": 90,
                                    "ativo": true,
                                    "dataCadastro": "2026-03-28T20:59:47.350Z"
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
                                        "nome: Nome do pacote é obrigatório",
                                        "servicoId: Serviço ID é obrigatório",
                                        "quantidadeSessoes: Quantidade de sessões precisa ser positivo",
                                        "preco: Preço precisa ser positivo",
                                        "validadeDias: Validade precisa ser positiva"
                                    ]
                                }
                            """)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Serviço não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "status": 404,
                                    "error": "NOT FOUND",
                                    "message": "Serviço não encontrado"
                                }
                            """)
                    )
            )
    })
    public ResponseEntity<PacoteResponseDTO> criar(@Valid @RequestBody PacoteRequestDTO dto) {
        Pacote entity = pacoteMapper.toEntity(dto);
        
        Pacote created = service.create(dto.getServicoId(), entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(pacoteMapper.toResponse(created));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pacote")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Pacote atualizado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "id": 1,
                                    "nome": "Depilação a laser 5x",
                                    "servico": {
                                        "id": 1,
                                        "nome": "Depilação a Laser",
                                        "descricao": "Depilação a Laser",
                                        "preco": 100,
                                        "duracaoMinutos": 30,
                                        "ativo": true
                                    },
                                    "quantidadeSessoes": 5,
                                    "preco": 110,
                                    "validadeDias": 90,
                                    "ativo": true,
                                    "dataCadastro": "2026-03-28T20:59:47.350Z"
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
                                        "nome: Nome do pacote é obrigatório",
                                        "servicoId: Serviço ID é obrigatório",
                                        "quantidadeSessoes: Quantidade de sessões precisa ser positivo",
                                        "preco: Preço precisa ser positivo",
                                        "validadeDias: Validade precisa ser positiva"
                                    ]
                                }
                            """)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "Serviço não encontrado", value = """
                                {
                                    "status": 404,
                                    "error": "NOT FOUND",
                                    "message": "Serviço não encontrado"
                                }
                            """),
                                    @ExampleObject(name = "Pacote não encontrado", value = """
                                {
                                    "status": 404,
                                    "error": "NOT FOUND",
                                    "message": "Pacote não encontrado"
                                }
                            """)
                            }
                    )
            )
    })
    public ResponseEntity<PacoteResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody PacoteRequestDTO dto) {
        Pacote entity = pacoteMapper.toEntity(dto);
        
        Pacote updated = service.update(id, entity);
        return ResponseEntity.ok(pacoteMapper.toResponse(updated));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir pacote")
    @ApiResponses({
            @ApiResponse(responseCode = "204",
                    description = "Pacote excluído"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Pacote não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "status": 404,
                                    "error": "NOT FOUND",
                                    "message": "Pacote não encontrado"
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
