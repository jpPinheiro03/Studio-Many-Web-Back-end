package com.studio.core.dominio.cliente_pacote.controller;

import com.studio.core.dominio.cliente_pacote.dto.ClientePacoteRequestDTO;
import com.studio.core.dominio.cliente_pacote.dto.ClientePacoteResponseDTO;
import com.studio.core.dominio.cliente_pacote.entity.ClientePacote;
import com.studio.core.service.ClientePacoteService;
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
import java.util.Map;

@RestController
@RequestMapping("/api/cliente-pacotes")
@Tag(name = "Pacotes do Cliente", description = "Endpoints para gestão de pacotes do cliente")
public class ClientePacoteController {
    
    @Autowired
    private ClientePacoteService service;
    
    @GetMapping
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Retorna todos os clientes, seus pacotes e sessões restantes",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
        [
          {
            "id": 1,
            "cliente": {
              "id": 1,
              "nome": "Adriana",
              "email": "adriana@gmail.com",
              "telefone": "11958484394",
              "cpf": "12345678901",
              "endereco": "R. Pedras cinzas",
              "observacoes": "Nenhum",
              "estagioFunil": "NOVO",
              "dataCadastro": "2026-04-03T03:09:59.544Z",
              "ativo": true
            },
            "pacote": {
              "id": 1,
              "nome": "Serviços de depilação a laser",
              "servico": {
                "id": 1,
                "nome": "Depliação a laser",
                "descricao": "Oferece serviço de depilação a laser",
                "preco": 159.99,
                "duracaoMinutos": 15,
                "ativo": true,
                "confirmacaoAutomatica": true
              },
              "quantidadeSessoes": 10,
              "preco": 689.99,
              "validadeDias": 0,
              "ativo": true,
              "dataCadastro": "2026-04-03T03:09:59.544Z"
            },
            "sessoesRestantes": 10,
            "status": "Pendente",
            "dataCompra": "2026-04-03T03:09:59.544Z",
            "dataValidade": "2026-04-03"
          },
          {
            "id": 2,
            "cliente": {
              "id": 2,
              "nome": "Fabiana",
              "email": "fabiana@gmail.com",
              "telefone": "11953314816",
              "cpf": "12345678901",
              "endereco": "R. Conceição",
              "observacoes": "Nenhum",
              "estagioFunil": "NOVO",
              "dataCadastro": "2026-04-03T03:09:59.544Z",
              "ativo": true
            },
            "pacote": {
              "id": 1,
              "nome": "Serviços de depilação a laser",
              "servico": {
                "id": 1,
                "nome": "Depliação a laser",
                "descricao": "Oferece serviço de depilação a laser",
                "preco": 159.99,
                "duracaoMinutos": 15,
                "ativo": true,
                "confirmacaoAutomatica": true
              },
              "quantidadeSessoes": 5,
              "preco": 389.99,
              "validadeDias": 0,
              "ativo": true,
              "dataCadastro": "2026-04-03T03:09:59.544Z"
            },
            "sessoesRestantes": 5,
            "status": "Pendente",
            "dataCompra": "2026-04-03T03:09:59.544Z",
            "dataValidade": "2026-04-03"
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
    public ResponseEntity<List<ClientePacoteResponseDTO>> listar() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/cliente/{id}")
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Retorna todos os clientes, seus pacotes e sessões restantes",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
        [
          {
            "id": 1,
            "cliente": {
              "id": 1,
              "nome": "Adriana",
              "email": "adriana@gmail.com",
              "telefone": "11958484394",
              "cpf": "12345678901",
              "endereco": "R. Pedras cinzas",
              "observacoes": "Nenhum",
              "estagioFunil": "NOVO",
              "dataCadastro": "2026-04-03T03:09:59.544Z",
              "ativo": true
            },
            "pacote": {
              "id": 1,
              "nome": "Serviços de depilação a laser",
              "servico": {
                "id": 1,
                "nome": "Depliação a laser",
                "descricao": "Oferece serviço de depilação a laser",
                "preco": 159.99,
                "duracaoMinutos": 15,
                "ativo": true,
                "confirmacaoAutomatica": true
              },
              "quantidadeSessoes": 10,
              "preco": 689.99,
              "validadeDias": 0,
              "ativo": true,
              "dataCadastro": "2026-04-03T03:09:59.544Z"
            },
            "sessoesRestantes": 10,
            "status": "Pendente",
            "dataCompra": "2026-04-03T03:09:59.544Z",
            "dataValidade": "2026-04-03"
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
            "message": "Cliente não encontrado com ID: 1"
            }
            ]
            """)
                    )
            )
    })
    public ResponseEntity<List<ClientePacoteResponseDTO>> porCliente(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByClienteId(id));
    }
    
    @GetMapping("/{id}")
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Retorna todos os clientes, seus pacotes e sessões restantes",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
        [
          {
            "id": 1,
            "cliente": {
              "id": 1,
              "nome": "Adriana",
              "email": "adriana@gmail.com",
              "telefone": "11958484394",
              "cpf": "12345678901",
              "endereco": "R. Pedras cinzas",
              "observacoes": "Nenhum",
              "estagioFunil": "NOVO",
              "dataCadastro": "2026-04-03T03:09:59.544Z",
              "ativo": true
            },
            "pacote": {
              "id": 1,
              "nome": "Serviços de depilação a laser",
              "servico": {
                "id": 1,
                "nome": "Depliação a laser",
                "descricao": "Oferece serviço de depilação a laser",
                "preco": 159.99,
                "duracaoMinutos": 15,
                "ativo": true,
                "confirmacaoAutomatica": true
              },
              "quantidadeSessoes": 10,
              "preco": 689.99,
              "validadeDias": 0,
              "ativo": true,
              "dataCadastro": "2026-04-03T03:09:59.544Z"
            },
            "sessoesRestantes": 10,
            "status": "Pendente",
            "dataCompra": "2026-04-03T03:09:59.544Z",
            "dataValidade": "2026-04-03"
          }
        ]""")
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
            "message": "Pacote do cliente não encontrado"
            }
            ]
            """)
                    )
            )
    })
    public ResponseEntity<ClientePacoteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
    
    @PostMapping
    @Operation(summary = "Criar pacote para cliente")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados para criação de pacote",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
            {
            "id": 1,
            "cliente": {
              "id": 1,
              "nome": "Adriana",
              "email": "adriana@gmail.com",
              "telefone": "11958484394",
              "cpf": "12345678901",
              "endereco": "R. Pedras cinzas",
              "observacoes": "Nenhum",
              "estagioFunil": "NOVO",
              "dataCadastro": "2026-04-03T03:09:59.544Z",
              "ativo": true
            },
            "pacote": {
              "id": 1,
              "nome": "Serviços de depilação a laser",
              "servico": {
                "id": 1,
                "nome": "Depliação a laser",
                "descricao": "Oferece serviço de depilação a laser",
                "preco": 159.99,
                "duracaoMinutos": 15,
                "ativo": true,
                "confirmacaoAutomatica": true
              },
              "quantidadeSessoes": 10,
              "preco": 689.99,
              "validadeDias": 0,
              "ativo": true,
              "dataCadastro": "2026-04-03T03:09:59.544Z"
            },
            "sessoesRestantes": 10,
            "status": "Pendente",
            "dataCompra": "2026-04-03T03:09:59.544Z",
            "dataValidade": "2026-04-03"
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
            "id": 1,
            "cliente": {
              "id": 1,
              "nome": "Adriana",
              "email": "adriana@gmail.com",
              "telefone": "11958484394",
              "cpf": "12345678901",
              "endereco": "R. Pedras cinzas",
              "observacoes": "Nenhum",
              "estagioFunil": "NOVO",
              "dataCadastro": "2026-04-03T03:09:59.544Z",
              "ativo": true
            },
            "pacote": {
              "id": 1,
              "nome": "Serviços de depilação a laser",
              "servico": {
                "id": 1,
                "nome": "Depliação a laser",
                "descricao": "Oferece serviço de depilação a laser",
                "preco": 159.99,
                "duracaoMinutos": 15,
                "ativo": true,
                "confirmacaoAutomatica": true
              },
              "quantidadeSessoes": 10,
              "preco": 689.99,
              "validadeDias": 0,
              "ativo": true,
              "dataCadastro": "2026-04-03T03:09:59.544Z"
            },
            "sessoesRestantes": 10,
            "status": "Pendente",
            "dataCompra": "2026-04-03T03:09:59.544Z",
            "dataValidade": "2026-04-03"
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
            "status": 404,
            "message": "Cliente não encontrado"
            }
            """)
                    )
            )
    })
    public ResponseEntity<ClientePacoteResponseDTO> criar(@Valid @RequestBody ClientePacoteRequestDTO dto) {
        ClientePacoteResponseDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PostMapping("/{id}/usar-sessao")
    @Operation(summary = "Usar sessão do pacote")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados para criação de pacote",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
            {
            "id": 1,
            "cliente": {
              "id": 1,
              "nome": "Adriana",
              "email": "adriana@gmail.com",
              "telefone": "11958484394",
              "cpf": "12345678901",
              "endereco": "R. Pedras cinzas",
              "observacoes": "Nenhum",
              "estagioFunil": "NOVO",
              "dataCadastro": "2026-04-03T03:09:59.544Z",
              "ativo": true
            },
            "pacote": {
              "id": 1,
              "nome": "Serviços de depilação a laser",
              "servico": {
                "id": 1,
                "nome": "Depliação a laser",
                "descricao": "Oferece serviço de depilação a laser",
                "preco": 159.99,
                "duracaoMinutos": 15,
                "ativo": true,
                "confirmacaoAutomatica": true
              },
              "quantidadeSessoes": 10,
              "preco": 689.99,
              "validadeDias": 0,
              "ativo": true,
              "dataCadastro": "2026-04-03T03:09:59.544Z"
            },
            "sessoesRestantes": 10,
            "status": "Pendente",
            "dataCompra": "2026-04-03T03:09:59.544Z",
            "dataValidade": "2026-04-03"
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
            "id": 1,
            "cliente": {
              "id": 1,
              "nome": "Adriana",
              "email": "adriana@gmail.com",
              "telefone": "11958484394",
              "cpf": "12345678901",
              "endereco": "R. Pedras cinzas",
              "observacoes": "Nenhum",
              "estagioFunil": "NOVO",
              "dataCadastro": "2026-04-03T03:09:59.544Z",
              "ativo": true
            },
            "pacote": {
              "id": 1,
              "nome": "Serviços de depilação a laser",
              "servico": {
                "id": 1,
                "nome": "Depliação a laser",
                "descricao": "Oferece serviço de depilação a laser",
                "preco": 159.99,
                "duracaoMinutos": 15,
                "ativo": true,
                "confirmacaoAutomatica": true
              },
              "quantidadeSessoes": 10,
              "preco": 689.99,
              "validadeDias": 0,
              "ativo": true,
              "dataCadastro": "2026-04-03T03:09:59.544Z"
            },
            "sessoesRestantes": 10,
            "status": "Pendente",
            "dataCompra": "2026-04-03T03:09:59.544Z",
            "dataValidade": "2026-04-03"
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
            "status": 404,
            "message": "Pacote do cliente não encontrado"
            }
            """)
                    )
            )
    })
    public ResponseEntity<ClientePacoteResponseDTO> usarSessao(@PathVariable Long id) {
        return ResponseEntity.ok(service.usarSessao(id));
    }
    
    @PutMapping("/{id}")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados para atualização",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
{
            "id": 1,
            "cliente": {
              "id": 1,
              "nome": "Adriana",
              "email": "adriana@gmail.com",
              "telefone": "11958484394",
              "cpf": "12345678901",
              "endereco": "R. Pedras cinzas",
              "observacoes": "Nenhum",
              "estagioFunil": "NOVO",
              "dataCadastro": "2026-04-03T03:09:59.544Z",
              "ativo": true
            },
            "pacote": {
              "id": 1,
              "nome": "Serviços de depilação a laser",
              "servico": {
                "id": 1,
                "nome": "Depliação a laser",
                "descricao": "Oferece serviço de depilação a laser",
                "preco": 159.99,
                "duracaoMinutos": 15,
                "ativo": true,
                "confirmacaoAutomatica": true
              },
              "quantidadeSessoes": 10,
              "preco": 689.99,
              "validadeDias": 0,
              "ativo": true,
              "dataCadastro": "2026-04-03T03:09:59.544Z"
            },
            "sessoesRestantes": 10,
            "status": "Pendente",
            "dataCompra": "2026-04-03T03:09:59.544Z",
            "dataValidade": "2026-04-03"
          }
        """)
            )
    )
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário é atualizado com êxito",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
{
            "id": 1,
            "cliente": {
              "id": 1,
              "nome": "Adriana",
              "email": "adriana@gmail.com",
              "telefone": "11958484394",
              "cpf": "12345678901",
              "endereco": "R. Pedras cinzas",
              "observacoes": "Nenhum",
              "estagioFunil": "NOVO",
              "dataCadastro": "2026-04-03T03:09:59.544Z",
              "ativo": true
            },
            "pacote": {
              "id": 1,
              "nome": "Serviços de depilação a laser",
              "servico": {
                "id": 1,
                "nome": "Depliação a laser",
                "descricao": "Oferece serviço de depilação a laser",
                "preco": 159.99,
                "duracaoMinutos": 15,
                "ativo": true,
                "confirmacaoAutomatica": true
              },
              "quantidadeSessoes": 10,
              "preco": 689.99,
              "validadeDias": 0,
              "ativo": true,
              "dataCadastro": "2026-04-03T03:09:59.544Z"
            },
            "sessoesRestantes": 10,
            "status": "Pendente",
            "dataCompra": "2026-04-03T03:09:59.544Z",
            "dataValidade": "2026-04-03"
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
            "message": "Cliente ID é obrigatório, Status inválido, Pacote ID é obrigatório"
            }
            """)
                    )
            )
    })
    public ResponseEntity<ClientePacoteResponseDTO> atualizar(@PathVariable Long id, @RequestBody Map<String, String> params) {
        ClientePacote.StatusClientePacote status;
        try {
            status = ClientePacote.StatusClientePacote.valueOf(params.get("status"));
        } catch (IllegalArgumentException e) {
            throw new com.studio.core.exception.BadRequestException("Status inválido: " + params.get("status"));
        }
        return ResponseEntity.ok(service.updateStatus(id, status));
    }
    
    @DeleteMapping("/{id}")
    @ApiResponses({

            @ApiResponse(
                    responseCode = "204",
                    description = "Deleta usuário com êxito",
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
            [
              {
            "status": 404,
            "error": "BAD REQUEST",
            "message": "Pacote do cliente não encontrado"
            }
            ]
            """)
                    )
            )
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
