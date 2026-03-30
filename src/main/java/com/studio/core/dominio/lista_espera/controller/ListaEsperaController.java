package com.studio.core.dominio.lista_espera.controller;

import com.studio.core.dominio.lista_espera.dto.ListaEsperaRequestDTO;
import com.studio.core.dominio.lista_espera.dto.ListaEsperaResponseDTO;
import com.studio.core.dominio.lista_espera.entity.ListaEspera;
import com.studio.core.dominio.lista_espera.mapper.ListaEsperaMapper;
import com.studio.core.service.ListaEsperaService;
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
@RequestMapping("/api/lista-espera")
@Tag(name = "Lista de Espera", description = "Endpoints para gestao de lista de espera")
public class ListaEsperaController {
    
    @Autowired
    private ListaEsperaService service;
    
    @Autowired
    private ListaEsperaMapper listaEsperaMapper;
    
    @GetMapping
    @Operation(summary = "Listar todos")
    @ApiResponses(
            @ApiResponse(responseCode = "200",
                    description = "Lista de espera encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                [
                                    {
                                        "id": 1,
                                        "cliente": {
                                            "id": 1,
                                            "nome": "João",
                                            "email": "joao@gmail.com",
                                            "telefone": "11987654321",
                                            "cpf": "22222222222",
                                            "endereco": "Rua João, 222",
                                            "observacoes": "",
                                            "estagioFunil": "Lead",
                                            "dataCadastro": "2026-03-28T19:36:36.495Z",
                                            "ativo": true
                                        },
                                        "servico": {
                                            "id": 1,
                                            "nome": "Depilação a Laser",
                                            "descricao": "Depilação a Laser",
                                            "preco": 100,
                                            "duracaoMinutos": 30,
                                            "ativo": true
                                        },
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
                                        "dataDesejada": "2026-03-28",
                                        "horarioDesejado": "08:00:00",
                                        "status": "Aguardando",
                                        "observacoes": "",
                                        "dataCadastro": "2026-03-28T19:36:36.495Z"
                                    }
                                ]
                            """)
                    )
            )
    )
    public ResponseEntity<List<ListaEsperaResponseDTO>> listar() {
        List<ListaEsperaResponseDTO> dtos = service.findAll().stream()
            .map(listaEsperaMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Listar por status")
    @ApiResponses(
            @ApiResponse(responseCode = "200",
                    description = "Lista de espera encontrada por status",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                [
                                    {
                                        "id": 1,
                                        "cliente": {
                                            "id": 1,
                                            "nome": "João",
                                            "email": "joao@gmail.com",
                                            "telefone": "11987654321",
                                            "cpf": "22222222222",
                                            "endereco": "Rua João, 222",
                                            "observacoes": "",
                                            "estagioFunil": "Lead",
                                            "dataCadastro": "2026-03-28T19:36:36.495Z",
                                            "ativo": true
                                        },
                                        "servico": {
                                            "id": 1,
                                            "nome": "Depilação a Laser",
                                            "descricao": "Depilação a Laser",
                                            "preco": 100,
                                            "duracaoMinutos": 30,
                                            "ativo": true
                                        },
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
                                        "dataDesejada": "2026-03-28",
                                        "horarioDesejado": "08:00:00",
                                        "status": "Aguardando",
                                        "observacoes": "",
                                        "dataCadastro": "2026-03-28T19:36:36.495Z"
                                    }
                                ]
                            """)
                    )
            )
    )
    public ResponseEntity<List<ListaEsperaResponseDTO>> porStatus(@PathVariable ListaEspera.StatusListaEspera status) {
        List<ListaEsperaResponseDTO> dtos = service.findByStatus(status).stream()
            .map(listaEsperaMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/aguardando")
    @Operation(summary = "Listar aguardando")
    @ApiResponses(
            @ApiResponse(responseCode = "200",
                    description = "Lista de espera encontrada com status aguardando",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                [
                                    {
                                        "id": 1,
                                        "cliente": {
                                            "id": 1,
                                            "nome": "João",
                                            "email": "joao@gmail.com",
                                            "telefone": "11987654321",
                                            "cpf": "22222222222",
                                            "endereco": "Rua João, 222",
                                            "observacoes": "",
                                            "estagioFunil": "Lead",
                                            "dataCadastro": "2026-03-28T19:36:36.495Z",
                                            "ativo": true
                                        },
                                        "servico": {
                                            "id": 1,
                                            "nome": "Depilação a Laser",
                                            "descricao": "Depilação a Laser",
                                            "preco": 100,
                                            "duracaoMinutos": 30,
                                            "ativo": true
                                        },
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
                                        "dataDesejada": "2026-03-28",
                                        "horarioDesejado": "08:00:00",
                                        "status": "Aguardando",
                                        "observacoes": "",
                                        "dataCadastro": "2026-03-28T19:36:36.495Z"
                                    }
                                ]
                            """)
                    )
            )
    )
    public ResponseEntity<List<ListaEsperaResponseDTO>> aguardando() {
        List<ListaEsperaResponseDTO> dtos = service.findAguardando().stream()
            .map(listaEsperaMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Listar por Id")
    @ApiResponses(
            @ApiResponse(responseCode = "200",
                    description = "Lista de espera encontrada por ID",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "id": 1,
                                    "cliente": {
                                        "id": 1,
                                        "nome": "João",
                                        "email": "joao@gmail.com",
                                        "telefone": "11987654321",
                                        "cpf": "22222222222",
                                        "endereco": "Rua João, 222",
                                        "observacoes": "",
                                        "estagioFunil": "Lead",
                                        "dataCadastro": "2026-03-28T19:36:36.495Z",
                                        "ativo": true
                                    },
                                    "servico": {
                                        "id": 1,
                                        "nome": "Depilação a Laser",
                                        "descricao": "Depilação a Laser",
                                        "preco": 100,
                                        "duracaoMinutos": 30,
                                        "ativo": true
                                    },
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
                                    "dataDesejada": "2026-03-28",
                                    "horarioDesejado": "08:00:00",
                                    "status": "Aguardando",
                                    "observacoes": "",
                                    "dataCadastro": "2026-03-28T19:36:36.495Z"
                                }
                            """)
                    )
            )
    )
    public ResponseEntity<ListaEsperaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(listaEsperaMapper.toResponse(service.findById(id)));
    }
    
    @PostMapping
    @Operation(summary = "Adicionar a lista de espera")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "201",
                            description = "Criado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = """
                                        {
                                            "id": 1,
                                            "cliente": {
                                                "id": 1,
                                                "nome": "João",
                                                "email": "joao@gmail.com",
                                                "telefone": "11987654321",
                                                "cpf": "22222222222",
                                                "endereco": "Rua João, 222",
                                                "observacoes": "",
                                                "estagioFunil": "Lead",
                                                "dataCadastro": "2026-03-28T19:36:36.495Z",
                                                "ativo": true
                                            },
                                            "servico": {
                                                "id": 1,
                                                "nome": "Depilação a Laser",
                                                "descricao": "Depilação a Laser",
                                                "preco": 100,
                                                "duracaoMinutos": 30,
                                                "ativo": true
                                            },
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
                                            "dataDesejada": "2026-03-28",
                                            "horarioDesejado": "08:00:00",
                                            "status": "Aguardando",
                                            "observacoes": "",
                                            "dataCadastro": "2026-03-28T19:36:36.495Z"
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
                                                "clienteId: Cliente ID é obrigatório",
                                                "servicoId: Serviço ID é obrigatório",
                                                "funcionarioId: Funcionario ID é obrigatório",
                                                "dataDesejada: Data desejada deve existir",
                                                "horarioDesejado: Horario desejado deve existir"
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
                                            @ExampleObject(name = "Cliente não encontrado", value = """
                                                {
                                                    "status": 404,
                                                    "error": "NOT FOUND",
                                                    "message": "Cliente não encontrado"
                                                }
                                            """),
                                            @ExampleObject(name = "Funcionário não encontrado", value = """
                                                {
                                                    "status": 404,
                                                    "error": "NOT FOUND",
                                                    "message": "Funcionário não encontrado"
                                                }
                                            """),
                                            @ExampleObject(name = "Serviço não encontrado", value = """
                                                {
                                                    "status": 404,
                                                    "error": "NOT FOUND",
                                                    "message": "Serviço não encontrado"
                                                }
                                            """)
                                    }
                            )
                    )
            }
    )
    public ResponseEntity<ListaEsperaResponseDTO> criar(@Valid @RequestBody ListaEsperaRequestDTO dto) {
        ListaEspera entity = listaEsperaMapper.toEntity(dto);
        
        ListaEspera created = service.create(dto.getClienteId(), dto.getServicoId(), dto.getFuncionarioId(), entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(listaEsperaMapper.toResponse(created));
    }
    
    @PutMapping("/{id}/atendido")
    @Operation(summary = "Marcar como atendido")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200",
                            description = "Horário de espera atendido",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = """
                                        {
                                            "id": 1,
                                            "cliente": {
                                                "id": 1,
                                                "nome": "João",
                                                "email": "joao@gmail.com",
                                                "telefone": "11987654321",
                                                "cpf": "22222222222",
                                                "endereco": "Rua João, 222",
                                                "observacoes": "",
                                                "estagioFunil": "Lead",
                                                "dataCadastro": "2026-03-28T19:36:36.495Z",
                                                "ativo": true
                                            },
                                            "servico": {
                                                "id": 1,
                                                "nome": "Depilação a Laser",
                                                "descricao": "Depilação a Laser",
                                                "preco": 100,
                                                "duracaoMinutos": 30,
                                                "ativo": true
                                            },
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
                                            "dataDesejada": "2026-03-28",
                                            "horarioDesejado": "08:00:00",
                                            "status": "Atendido",
                                            "observacoes": "",
                                            "dataCadastro": "2026-03-28T19:36:36.495Z"
                                        }
                                    """)
                            )
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Lista de espera não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = """
                                {
                                    "status": 404,
                                    "error": "NOT FOUND",
                                    "message": "Lista de espera não encontrada"
                                }
                            """)
                            )
                    )
            }
    )
    public ResponseEntity<ListaEsperaResponseDTO> marcarAtendido(@PathVariable Long id) {
        return ResponseEntity.ok(listaEsperaMapper.toResponse(service.marcarAtendido(id)));
    }
    
    @PutMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200",
                            description = "Horário de espera cancelado",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = """
                                        {
                                            "id": 1,
                                            "cliente": {
                                                "id": 1,
                                                "nome": "João",
                                                "email": "joao@gmail.com",
                                                "telefone": "11987654321",
                                                "cpf": "22222222222",
                                                "endereco": "Rua João, 222",
                                                "observacoes": "",
                                                "estagioFunil": "Lead",
                                                "dataCadastro": "2026-03-28T19:36:36.495Z",
                                                "ativo": true
                                            },
                                            "servico": {
                                                "id": 1,
                                                "nome": "Depilação a Laser",
                                                "descricao": "Depilação a Laser",
                                                "preco": 100,
                                                "duracaoMinutos": 30,
                                                "ativo": true
                                            },
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
                                            "dataDesejada": "2026-03-28",
                                            "horarioDesejado": "08:00:00",
                                            "status": "Cancelado",
                                            "observacoes": "",
                                            "dataCadastro": "2026-03-28T19:36:36.495Z"
                                        }
                                    """)
                            )
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Lista de espera não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = """
                                {
                                    "status": 404,
                                    "error": "NOT FOUND",
                                    "message": "Lista de espera não encontrada"
                                }
                            """)
                            )
                    )
            }
    )
    public ResponseEntity<ListaEsperaResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(listaEsperaMapper.toResponse(service.cancelar(id)));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "204",
                            description = "Excluído"
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Lista de espera não encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = """
                                {
                                    "status": 404,
                                    "error": "NOT FOUND",
                                    "message": "Lista de espera não encontrada"
                                }
                            """)
                            )
                    )
            }
    )
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
