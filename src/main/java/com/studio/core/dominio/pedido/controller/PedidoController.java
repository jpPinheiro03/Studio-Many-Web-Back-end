package com.studio.core.dominio.pedido.controller;

import com.studio.core.dominio.pedido.dto.PedidoRequestDTO;
import com.studio.core.dominio.pedido.dto.PedidoResponseDTO;
import com.studio.core.dominio.pedido.entity.Pedido;
import com.studio.core.dominio.pedido.mapper.PedidoMapper;
import com.studio.core.service.PedidoService;
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
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Endpoints para gestao de pedidos")
public class PedidoController {
    
    @Autowired
    private PedidoService service;
    
    @Autowired
    private PedidoMapper pedidoMapper;
    
    @GetMapping
    @Operation(summary = "Listar pedidos")
    @ApiResponses(
            @ApiResponse(responseCode = "200",
                    description = "Pedidos encontrados",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(value = """
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
                                                "itens":[
                                                    {
                                                        "id": 1,
                                                        "produto": {
                                                            "id": 1,
                                                            "nome": "Espumante",
                                                            "descricao": "Espumante para rosto",
                                                            "preco": 50,
                                                            "estoque": 100,
                                                            "ativo": true,
                                                            "dataCadastro": "2026-03-29T02:50:55.005Z"
                                                            },
                                                        "quantidade": 6,
                                                        "preco": 50
                                                    }
                                                ],
                                            "valorTotal": 300,
                                            "status": "PAGO",
                                            "dataPedido": "2026-03-29T02:50:55.005Z"
                                            }
                                        ]
                                    """)
                            }
                    )
            )
    )
    public ResponseEntity<List<PedidoResponseDTO>> listar(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        List<Pedido> pedidos;
        if (page != null) {
            pedidos = service.findPaginated(page, size);
        } else {
            pedidos = service.findAll();
        }
        List<PedidoResponseDTO> dtos = pedidos.stream()
            .map(pedidoMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/cliente/{id}")
    @Operation(summary = "Listar pedidos por ID do cliente")
    @ApiResponses(
            @ApiResponse(responseCode = "200",
                    description = "Pedidos encontrados por ID do cliente",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(value = """
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
                                                "itens":[
                                                    {
                                                        "id": 1,
                                                        "produto": {
                                                            "id": 1,
                                                            "nome": "Espumante",
                                                            "descricao": "Espumante para rosto",
                                                            "preco": 50,
                                                            "estoque": 100,
                                                            "ativo": true,
                                                            "dataCadastro": "2026-03-29T02:50:55.005Z"
                                                            },
                                                        "quantidade": 6,
                                                        "preco": 50
                                                    }
                                                ],
                                            "valorTotal": 300,
                                            "status": "PAGO",
                                            "dataPedido": "2026-03-29T02:50:55.005Z"
                                            }
                                        ]
                                    """)
                            }
                    )
            )
    )
    public ResponseEntity<List<PedidoResponseDTO>> porCliente(@PathVariable Long id) {
        List<PedidoResponseDTO> dtos = service.findByClienteId(id).stream()
            .map(pedidoMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Listar pedido por ID")
    @ApiResponses(
            @ApiResponse(responseCode = "200",
                    description = "Pedido encontrado por ID",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(value = """
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
                                            "itens":[
                                                {
                                                    "id": 1,
                                                    "produto": {
                                                        "id": 1,
                                                        "nome": "Espumante",
                                                        "descricao": "Espumante para rosto",
                                                        "preco": 50,
                                                        "estoque": 100,
                                                        "ativo": true,
                                                        "dataCadastro": "2026-03-29T02:50:55.005Z"
                                                        },
                                                    "quantidade": 6,
                                                    "preco": 50
                                                }
                                            ],
                                        "valorTotal": 300,
                                        "status": "PAGO",
                                        "dataPedido": "2026-03-29T02:50:55.005Z"
                                        }
                                    """)
                            }
                    )
            )
    )
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoMapper.toResponse(service.findById(id)));
    }
    
    @PostMapping
    @Operation(summary = "Criar pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Pedido criado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(value = """
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
                                            "itens":[
                                                {
                                                    "id": 1,
                                                    "produto": {
                                                        "id": 1,
                                                        "nome": "Espumante",
                                                        "descricao": "Espumante para rosto",
                                                        "preco": 50,
                                                        "estoque": 100,
                                                        "ativo": true,
                                                        "dataCadastro": "2026-03-29T02:50:55.005Z"
                                                        },
                                                    "quantidade": 6,
                                                    "preco": 50
                                                }
                                            ],
                                        "valorTotal": 300,
                                        "status": "PAGO",
                                        "dataPedido": "2026-03-29T02:50:55.005Z"
                                        }
                                    """)
                            }
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
                                                "itens: Itens são obrigatórios",
                                                "produtoId: Produto ID é obrigatório",
                                                "quantidade: Quantidade precisa ser positiva"
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
                                    @ExampleObject(name = "Produto não encontrado", value = """
                                        {
                                            "status": 404,
                                            "error": "NOT FOUND",
                                            "message": "Produto não encontrado"
                                        }
                                    """)
                            }
                    )
            )
    })
    public ResponseEntity<PedidoResponseDTO> criar(@Valid @RequestBody PedidoRequestDTO dto) {
        Pedido created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoMapper.toResponse(created));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar status do pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Pedido atualizado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(value = """
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
                                            "itens":[
                                                {
                                                    "id": 1,
                                                    "produto": {
                                                        "id": 1,
                                                        "nome": "Espumante",
                                                        "descricao": "Espumante para rosto",
                                                        "preco": 50,
                                                        "estoque": 100,
                                                        "ativo": true,
                                                        "dataCadastro": "2026-03-29T02:50:55.005Z"
                                                        },
                                                    "quantidade": 6,
                                                    "preco": 50
                                                }
                                            ],
                                        "valorTotal": 300,
                                        "status": "CANCELADO",
                                        "dataPedido": "2026-03-29T02:50:55.005Z"
                                        }
                                    """)
                            }
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Pedido não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "status": 404,
                                    "error": "NOT FOUND",
                                    "message": "Pedido não encontrado"
                                }
                            """)
                    )
            )
    })
    public ResponseEntity<PedidoResponseDTO> atualizar(@PathVariable Long id, @RequestBody Map<String, String> params) {
        Pedido.StatusPedido status;
        try {
            status = Pedido.StatusPedido.valueOf(params.get("status"));
        } catch (IllegalArgumentException e) {
            throw new com.studio.core.exception.BadRequestException("Status inválido: " + params.get("status"));
        }
        return ResponseEntity.ok(pedidoMapper.toResponse(service.updateStatus(id, status)));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar pedido por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204",
                    description = "Pedido deletado"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Pedido não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "status": 404,
                                    "error": "NOT FOUND",
                                    "message": "Pedido não encontrado"
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
