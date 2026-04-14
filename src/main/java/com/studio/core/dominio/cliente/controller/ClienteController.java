package com.studio.core.dominio.cliente.controller;

import com.studio.core.dominio.cliente.dto.ClienteRequestDTO;
import com.studio.core.dominio.cliente.dto.ClienteResponseDTO;
import com.studio.core.dominio.cliente.entity.Cliente;
import com.studio.core.dominio.cliente.mapper.ClienteMapper;
import com.studio.core.service.ClienteService;
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

@RestController // Esta classe aceita requisições HTTP e retorna JSON
@RequestMapping("/api/clientes") // URL base para todos os endpoints desta classe
@Tag(name = "Clientes", description = "Endpoints para gestão de clientes") // Swagger: agrupa endpoints por categoria
public class ClienteController {

    @Autowired // Injeta dependência automaticamente (injeção de dependência)
    private ClienteService service;

    @Autowired // Injeta dependência automaticamente (injeção de dependência)
    private ClienteMapper clienteMapper;

    @GetMapping // Endpoint que aceita requisições GET
    @Operation(summary = "Listar clientes", description = "Retorna todos os clientes") // Swagger: descreve o endpoint na documentação
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Retorna todos os clientes",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
            [
              {
                "id": 1,
                "nome": "Eduardo da Silva",
                "email": "eduardo@hotmail.com",
                "telefone": "11961969926",
                "cpf": "12345678901",
                "endereco": "R. Haddock Lobo, 589",
                "observacoes": "Nenhum",
                "estagioFunil": "Inspeção de serviço",
                "dataCadastro": "2026-04-03T02:13:09.926Z",
                "ativo": true
              }
            ]
            """)
                    )
            ),

            @ApiResponse(
                    responseCode = "204",
                    description = "Nenhum cliente é encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
            [
              {}
            ]
            """)
                    )
            )
    })
    public ResponseEntity<List<ClienteResponseDTO>> listar(
            @RequestParam(required = false) Integer page, // Recebe o valor do parâmetro da URL (?param=valor)
            @RequestParam(required = false, defaultValue = "20") Integer size) { // Recebe o valor do parâmetro com valor padrão
        List<Cliente> clientes;
        if (page != null) {
            clientes = service.findPaginated(page, size);
        } else {
            clientes = service.findAll();
        }
        List<ClienteResponseDTO> dtos = clientes.stream()
                .map(clienteMapper::toResponse)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}") // Endpoint que aceita requisições GET com parâmetro na URL
    @Operation(summary = "Buscar cliente por ID") // Swagger: descreve o endpoint na documentação
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Retorna o cliente especificado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
            [
              {
                "id": 1,
                "nome": "Eduardo da Silva",
                "email": "eduardo@hotmail.com",
                "telefone": "11961969926",
                "cpf": "12345678901",
                "endereco": "R. Haddock Lobo, 589",
                "observacoes": "Nenhum",
                "estagioFunil": "Inspeção de serviço",
                "dataCadastro": "2026-04-03T02:13:09.926Z",
                "ativo": true
              }
            ]
            """)
                    )
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
            [
              {
            "status": 404,
            "error": "BAD REQUEST",
            "message": "Cliente não encontrado com ID: 1""
            }
            ]
            """)
                    )
            )
    })
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) { // Recebe o valor da URL (ex: /clientes/{id})
        Cliente entity = service.findById(id);
        return ResponseEntity.ok(clienteMapper.toResponse(entity));
    }

    @PostMapping // Endpoint que aceita requisições POST (criar)
    @Operation(summary = "Criar cliente", description = "Cadastra um novo cliente") // Swagger: descreve o endpoint na documentação
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados para cadastro",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
            {
              "nome": "Raquel Medeiros",
              "email": "raquel@gmail.com",
              "telefone": "1198402952",
              "cpf": "12345678901",
              "endereco": "R. Almirantes, 55",
              "observacoes": "Nenhum",
              "ativo": true
            }
        """)
            )
    )
    @ApiResponses({

            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário é criado com êxito",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                            {
              "id": 1
              "nome": "Raquel Medeiros",
              "email": "raquel@gmail.com",
              "telefone": "1198402952",
              "cpf": "12345678901",
              "endereco": "R. Almirantes, 55",
              "observacoes": "Nenhum",
              "ativo": true
                                    }
            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito de entidade",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
            {
            "status": 409,
            "message": "Email já cadastrado"
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
            "message": "Nome é obrigatório, Email inválido, Telefone é obrigatório"
            }
            """)
                    )
            )
    })
    public ResponseEntity<ClienteResponseDTO> criar(@Valid @RequestBody ClienteRequestDTO dto) { // Valida os campos e recebe os dados do corpo da requisição HTTP (JSON)
        Cliente entity = clienteMapper.toEntity(dto);
        Cliente created = service.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteMapper.toResponse(created));
    }

    @PutMapping("/{id}") // Endpoint que aceita requisições PUT (atualizar) com parâmetro na URL
    @Operation(summary = "Atualizar cliente") // Swagger: descreve o endpoint na documentação
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados para atualização",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
            {
              "nome": "Raquel Medeiros",
              "email": "raquel@gmail.com",
              "telefone": "1198402952",
              "cpf": "12345678901",
              "endereco": "R. Almirantes, 55",
              "observacoes": "Nenhum",
              "ativo": true
            }
        """)
            )
    )
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário é criado com êxito",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                {
                  "nome": "Thaisa",
                  "email": "thaisa@gmail.com",
                  "telefone": "11193930302",
                  "cpf": "12345678901",
                  "endereco": "R. Ramos Figueiros, 12",
                  "observacoes": "Nenhum",
                  "ativo": true
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
            "message": "Email inválido"
            }
            """)
                    )
            )
    })
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO dto) { // Recebe o valor da URL e valida os dados do corpo da requisição HTTP (JSON)
        Cliente entity = clienteMapper.toEntity(dto);
        Cliente updated = service.update(id, entity);
        return ResponseEntity.ok(clienteMapper.toResponse(updated));
    }

    @GetMapping("/telefone/{telefone}") // Endpoint que aceita requisições GET com parâmetro na URL
    @Operation(summary = "Buscar cliente por telefone") // Swagger: descreve o endpoint na documentação
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Retorna o cliente especificado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
            [
              {
                "id": 1,
                "nome": "Eduardo da Silva",
                "email": "eduardo@hotmail.com",
                "telefone": "11961969926",
                "cpf": "12345678901",
                "endereco": "R. Haddock Lobo, 589",
                "observacoes": "Nenhum",
                "estagioFunil": "Inspeção de serviço",
                "dataCadastro": "2026-04-03T02:13:09.926Z",
                "ativo": true
              }
            ]
            """)
                    )
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
            [
              {
            "status": 404,
            "error": "NOT FOUND",
            "message": "Cliente não encontrado com ID: 1"
            }
            ]
            """)
                    )
            )
    })
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorTelefone(@PathVariable String telefone) { // Recebe o valor da URL (ex: /clientes/telefone/{telefone})
        List<ClienteResponseDTO> dtos = service.findByTelefone(telefone).stream()
                .map(clienteMapper::toResponse)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/busca") // Endpoint que aceita requisições GET
    @Operation(summary = "Buscar clientes por nome") // Swagger: descreve o endpoint na documentação
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Retorna o cliente especificado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
            [
              {
                "id": 1,
                "nome": "Eduardo da Silva",
                "email": "eduardo@hotmail.com",
                "telefone": "11961969926",
                "cpf": "12345678901",
                "endereco": "R. Haddock Lobo, 589",
                "observacoes": "Nenhum",
                "estagioFunil": "Inspeção de serviço",
                "dataCadastro": "2026-04-03T02:13:09.926Z",
                "ativo": true
              }
            ]
            """)
                    )
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
            [
              {
            "status": 404,
            "error": "NOT FOUND",
            "message": "Cliente não encontrado com ID: 1"
            }
            ]
            """)
                    )
            )
    })
    public ResponseEntity<List<ClienteResponseDTO>> buscarPorNome(@RequestParam String q) { // Recebe o valor do parâmetro da URL (?param=valor)
        List<ClienteResponseDTO> dtos = service.findByNomeContaining(q).stream()
                .map(clienteMapper::toResponse)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/inativos") // Endpoint que aceita requisições GET
    @Operation(summary = "Listar clientes inativos") // Swagger: descreve o endpoint na documentação
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Retorna os clientes inativos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
            [
              {
                "id": 1,
                "nome": "Geovana Queiroz",
                "email": "geovana@hotmail.com",
                "telefone": "11961969926",
                "cpf": "12345678901",
                "endereco": "R. Haddock Lobo, 589",
                "observacoes": "Nenhum",
                "estagioFunil": "Inspeção de serviço",
                "dataCadastro": "2026-04-03T02:13:09.926Z",
                "ativo": true
              },
              {
                "id": 2,
                "nome": "Maria Cardoso",
                "email": "maria@hotmail.com",
                "telefone": "11951912426",
                "cpf": "12345678901",
                "endereco": "R. Haddock Lobo, 589",
                "observacoes": "Nenhum",
                "estagioFunil": "Inspeção de serviço",
                "dataCadastro": "2026-04-03T02:13:09.926Z",
                "ativo": true
              }
            ]
            """)
                    )
            ),

            @ApiResponse(
                    responseCode = "204",
                    description = "Nenhum cliente está inativo",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
            [
              {}
            ]
            """)
                    )
            )
    })
    public ResponseEntity<List<ClienteResponseDTO>> inativos(@RequestParam(defaultValue = "90") int dias) { // Recebe o valor do parâmetro da URL com valor padrão
        List<ClienteResponseDTO> dtos = service.findInativos(dias).stream()
                .map(clienteMapper::toResponse)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}") // Endpoint que aceita requisições DELETE (excluir) com parâmetro na URL
    @Operation(summary = "Excluir cliente") // Swagger: descreve o endpoint na documentação
    @ApiResponses({

            @ApiResponse(
                    responseCode = "204",
                    description = "Cliente é excluído com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
            [
              {}
            ]
            """)
                    )
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
            [
              {
            "status": 404,
            "error": "NOT FOUND",
            "message": "Cliente não encontrado com ID: 1"
            }
            ]
            """)
                    )
            )
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) { // Recebe o valor da URL (ex: /clientes/{id})
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
