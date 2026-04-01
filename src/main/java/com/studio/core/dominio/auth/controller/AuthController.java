package com.studio.core.dominio.auth.controller;

import com.studio.core.dominio.usuario.dto.LoginRequestDTO;
import com.studio.core.dominio.usuario.dto.LoginResponseDTO;
import com.studio.core.dominio.usuario.dto.UsuarioRequestDTO;
import com.studio.core.dominio.usuario.dto.UsuarioResponseDTO;
import com.studio.core.dominio.usuario.entity.Usuario;
import com.studio.core.service.UsuarioService;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController // Esta classe aceita requisições HTTP e retorna JSON
@RequestMapping("/api/auth") // URL base para todos os endpoints desta classe
@Tag(name = "Autenticação", description = "Endpoints para autenticação e gerenciamento de usuários") // Swagger: agrupa endpoints por categoria
public class AuthController {

    @Autowired // Injeta dependência automaticamente (injeção de dependência)
    private UsuarioService service;

    @PostMapping("/cadastro") // Endpoint que aceita requisições POST (criar) no caminho /cadastro
    @Operation(summary = "Cadastrar usuário (apenas ADMIN)") // Swagger: descreve o endpoint na documentação
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados para cadastro",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
        {
          "nome": "Isabelly",
          "dataInicio": "2026-03-29T20:36:32.949Z",
          "dataFim": "2026-04-29T20:36:32.949Z",
          "motivo": "Férias"
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
                "nome": "Isabelly",
                "dataInicio": "2026-03-29T20:36:32.949Z",
                "dataFim": "2026-04-29T20:36:32.949Z",
                "Motivo": "Férias"
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
            "message": "Corpo da requisição inválido ou malformado",
            "timestamp": "2026-03-31T23:01:32.16511267"
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
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody UsuarioRequestDTO dto) { // Valida os campos e recebe os dados do corpo da requisição HTTP (JSON)
        UsuarioResponseDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PostMapping("/login") // Endpoint que aceita requisições POST (criar) no caminho /login
    @Operation(summary = "Login") // Swagger: descreve o endpoint na documentação
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário faz login com êxito",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                {
                  "token": "F36qStf72.fjai32ya9.vdsbq81wsA.vsbjweh32-324U80bdv9IO3",
                  "usuario": {
                    "id": 1,
                    "email": "admin@studio.com",
                    "role": "ADMIN",
                    "funcionarioId": 1,
                    "nomeFuncionario": "Administrador",
                    "dataCadastro": "2026-03-31T20:52:32.999611",
                    "ativo": true
                  },
                  "tipo": "Bearer"
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
              "message": "Email ou senha inválidos",
              "timestamp": "2026-04-01T02:17:46.1077598"
            }
            """)
                    )
            ),
    })
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) { // Valida os campos e recebe os dados do corpo da requisição HTTP (JSON)
        LoginResponseDTO loginResponse = service.login(dto);
        return ResponseEntity.ok(loginResponse);
    }
    
    @GetMapping("/me") // Endpoint que aceita requisições GET no caminho /me
    @Operation(summary = "Usuário atual") // Swagger: descreve o endpoint na documentação
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário atual logado é exibido",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                {
                  "id": 1,
                  "email": "admin@studio.com",
                  "role": "ADMIN",
                  "funcionarioId": 1,
                  "nomeFuncionario": "Administrador",
                  "dataCadastro": "2026-03-31T20:52:32.999611",
                  "ativo": true
                }
            """)
                    )
            )
    })

    public ResponseEntity<UsuarioResponseDTO> getCurrentUser(Authentication authentication) { // Recebe o objeto de autenticação do Spring Security
        return ResponseEntity.ok(service.getCurrentUser(authentication.getName()));
    }

    @GetMapping("/usuarios") // Endpoint que aceita requisições GET no caminho /usuarios
    @Operation(summary = "Listar usuários") // Swagger: descreve o endpoint na documentação
    @ApiResponse(
            responseCode = "200",
            description = "Retorna os usuários do sistema",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            [
                               {
                                "id": 1,
                                "email": "admin@studio.com",
                                 "role": "ADMIN",
                                 "funcionarioId": 1,
                                 "nomeFuncionario": "Administrador",
                                 "dataCadastro": "2026-03-31T20:52:32.999611",
                                 "ativo": true
                                  },
                                  {
                                   "id": 2,
                                   "email": "funcionario@studio.com",
                                   "role": "FUNCIONARIO",
                                   "funcionarioId": 1,
                                   "nomeFuncionario": "Administrador",
                                   "dataCadastro": "2026-03-31T22:37:11.526969",
                                   "ativo": true
                                        }
                                   ]
            """)
            )
    )
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/usuarios/{id}") // Endpoint que aceita requisições GET com parâmetro na URL
    @Operation(summary = "Buscar usuário por ID") // Swagger: descreve o endpoint na documentação
    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário especificado é atualizado com êxito",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                {
                  "id": 1,
                  "email": "admin@studio.com",
                  "role": "ADMIN",
                  "funcionarioId": 1,
                  "nomeFuncionario": "Administrador",
                  "dataCadastro": "2026-03-31T20:52:32.999611",
                  "ativo": true
                }
            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso não encontrado inválida",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                {
                  "status": 404,
                  "message": "Usuário não encontrado com ID: 4",
                  "timestamp": "2026-04-01T02:37:06.03697253"
                }
            """)
                    )
            ),
    })
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) { // Recebe o valor da URL (ex: /usuarios/{id})
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/usuarios/{id}") // Endpoint que aceita requisições PUT (atualizar) com parâmetro na URL
    @Operation(summary = "Atualizar usuário") // Swagger: descreve o endpoint na documentação
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados para atualização",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = {

                            @ExampleObject(
                                    name = "200 - êxito",
                                    summary = "Usuário é atualizado com êxito",
                                    value = """
                {
                  "email": "admin@studio.com",
                  "role": "ADMIN"
                }
                """
                            ),

                            @ExampleObject(
                                    name = "400 - Requisição inválida",
                                    summary = "Campos inválidos ou malformados",
                                    value = """
                {
                  "email": "",
                  "role": null
                }
                """
                            ),

                            @ExampleObject(
                                    name = "404 - Usuário não encontrado",
                                    summary = "ID não existe (body válido, mas recurso inexistente)",
                                    value = """
                {
                  "email": "naoexiste@studio.com",
                  "role": "ADMIN"
                }
                """
                            )
                    }
            )
    )

    @ApiResponses({

            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário especificado é atualiazado com êxito",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
            {
              "id": 1,
              "email": "admin@studio.com",
              "role": "ADMIN",
              "funcionarioId": 1,
              "nomeFuncionario": "Administrador",
              "dataCadastro": "2026-03-31T20:52:32.999611",
              "ativo": true
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
              "message": "Usuário não encontrado com ID: 4",
              "timestamp": "2026-04-01T02:37:06.03697253"
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
              "message": "Corpo da requisição inválido ou malformado",
              "timestamp": "2026-04-01T02:44:25.604846916"
            }
            """)
                    )
            )
    })
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Long id, @RequestBody UsuarioRequestDTO dto) { // Recebe o valor da URL e os dados do corpo da requisição HTTP (JSON)
        return ResponseEntity.ok(service.update(id, dto));
    }

    @PutMapping("/usuarios/{id}/trocar-senha") // Endpoint que aceita requisições PUT (atualizar) com parâmetro na URL
    @Operation(summary = "Trocar senha do usuário") // Swagger: descreve o endpoint na documentação
    @ApiResponses({

            @ApiResponse(
                    responseCode = "204",
                    description = "Usuário troca a senha com êxito",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                            {}
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
            "message": "Nova senha deve ter no mínimo 6 caracteres",
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
            "message": "Senha atual incorreta",
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
            "message": "Usuário não encontrado"
            }
            """)
                    )
            )
    })
    public ResponseEntity<Void> trocarSenha(@PathVariable Long id, @RequestBody Map<String, String> body) { // Recebe o valor da URL e os dados do corpo da requisição HTTP (JSON)
        String senhaAtual = body.get("senhaAtual");
        String novaSenha = body.get("novaSenha");
        if (novaSenha == null || novaSenha.length() < 6) {
            throw new com.studio.core.exception.BadRequestException("Nova senha deve ter no mínimo 6 caracteres");
        }
        service.changePassword(id, senhaAtual, novaSenha);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/usuarios/{id}") // Endpoint que aceita requisições DELETE (excluir) com parâmetro na URL
    @Operation(summary = "Excluir usuário") // Swagger: descreve o endpoint na documentação
    @ApiResponses({

            @ApiResponse(
                    responseCode = "204",
                    description = "Usuário é deletado com êxito",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                            {}
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
            "message": "Não é possível excluir o último administrador do sistema"
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
            "error": "Requisição inválida",
            "message": "Usuário não encontrado com ID: 4""
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
            "message": "Usuário não encontrado"
            }
            """)
                    )
            )
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) { // Recebe o valor da URL (ex: /usuarios/{id})
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
