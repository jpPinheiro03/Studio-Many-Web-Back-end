package com.studio.core.dominio.agendamento.controller;

import com.studio.core.dominio.agendamento.dto.AgendamentoRequestDTO;
import com.studio.core.dominio.agendamento.dto.AgendamentoResponseDTO;
import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.agendamento.entity.StatusAgendamento;
import com.studio.core.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
@Tag(name = "Agendamentos", description = "Endpoints para gestão de agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;



    @GetMapping
    @Operation(summary = "Listar todos os agendamentos")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de agendamentos retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_LISTA_AGENDAMENTOS)
                    )
            )
    })
    public ResponseEntity<List<AgendamentoResponseDTO>> listar() {
        List<AgendamentoResponseDTO> dtos = service.findAll().stream()
                .map(AgendamentoResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar agendamento por ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamento encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_AGENDAMENTO)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agendamento não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_404_AGENDAMENTO)
                    )
            )
    })
    public ResponseEntity<AgendamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoResponseDTO.fromEntity(service.findById(id)));
    }

    @PostMapping
    @Operation(summary = "Criar agendamento")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Agendamento criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_AGENDAMENTO)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_400)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente, serviço ou funcionário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_404_CLIENTE)
                    )
            )
    })
    public ResponseEntity<AgendamentoResponseDTO> criar(@Valid @RequestBody AgendamentoRequestDTO dto) {
        Agendamento entity = new Agendamento();
        entity.setDataHoraInicio(dto.getDataHoraInicio());
        entity.setDataHoraFim(dto.getDataHoraFim());
        entity.setValorTotal(dto.getValorTotal());
        entity.setValorSinal(dto.getValorSinal());
        entity.setQuantidadeParcelas(dto.getQuantidadeParcelas());
        entity.setObservacoes(dto.getObservacoes());

        Agendamento created = service.create(dto.getClienteId(), dto.getServicoId(), dto.getFuncionarioId(), entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(AgendamentoResponseDTO.fromEntity(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar agendamento")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamento atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_AGENDAMENTO)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_400)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agendamento não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_404_AGENDAMENTO)
                    )
            )
    })
    public ResponseEntity<AgendamentoResponseDTO> atualizar(@PathVariable Long id, @RequestBody AgendamentoRequestDTO dto) {
        Agendamento entity = service.findById(id);
        entity.setDataHoraInicio(dto.getDataHoraInicio());
        entity.setDataHoraFim(dto.getDataHoraFim());
        entity.setValorTotal(dto.getValorTotal());
        entity.setValorSinal(dto.getValorSinal());
        entity.setQuantidadeParcelas(dto.getQuantidadeParcelas());
        entity.setObservacoes(dto.getObservacoes());
        return ResponseEntity.ok(AgendamentoResponseDTO.fromEntity(service.update(id, entity)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir agendamento")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Agendamento excluído com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agendamento não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_404_AGENDAMENTO)
                    )
            )
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    @Operation(
            summary = "Atualizar status do agendamento",
            description = "Status disponíveis: PENDENTE, CONFIRMADO, CANCELADO, CONCLUIDO, NAO_COMPARECEU"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Status atualizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_AGENDAMENTO)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Status inválido",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 400,
                                        "error": "BAD REQUEST",
                                        "message": "Status inválido. Valores aceitos: PENDENTE, CONFIRMADO, CANCELADO, CONCLUIDO, NAO_COMPARECEU"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agendamento não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_404_AGENDAMENTO)
                    )
            )
    })
    public ResponseEntity<AgendamentoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusAgendamento status) {
        return ResponseEntity.ok(AgendamentoResponseDTO.fromEntity(service.updateStatus(id, status)));
    }

    @PostMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar agendamento", description = "Muda o status do agendamento para CONFIRMADO")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamento confirmado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 1,
                                        "cliente": { "id": 1, "nome": "Maria Silva" },
                                        "servico": {
                                            "id": 1,
                                            "nome": "Drenagem Linfática",
                                            "descricao": "Drenagem linfática corporal",
                                            "preco": 120.00,
                                            "duracaoMinutos": 60,
                                            "ativo": true
                                        },
                                        "funcionario": { "id": 1, "nome": "Ana Souza" },
                                        "dataHoraInicio": "2026-04-10T14:00:00",
                                        "dataHoraFim": "2026-04-10T15:00:00",
                                        "status": "CONFIRMADO",
                                        "valorTotal": 120.00,
                                        "valorSinal": 0.00,
                                        "quantidadeParcelas": 1,
                                        "motivoCancelamento": null,
                                        "observacoes": "Primeira sessão"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agendamento não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_404_AGENDAMENTO)
                    )
            )
    })
    public ResponseEntity<AgendamentoResponseDTO> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoResponseDTO.fromEntity(service.updateStatus(id, StatusAgendamento.CONFIRMADO)));
    }

    @PutMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar agendamento", description = "Muda o status do agendamento para CANCELADO")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamento cancelado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 1,
                                        "cliente": { "id": 1, "nome": "Maria Silva" },
                                        "servico": {
                                            "id": 1,
                                            "nome": "Drenagem Linfática",
                                            "descricao": "Drenagem linfática corporal",
                                            "preco": 120.00,
                                            "duracaoMinutos": 60,
                                            "ativo": true
                                        },
                                        "funcionario": { "id": 1, "nome": "Ana Souza" },
                                        "dataHoraInicio": "2026-04-10T14:00:00",
                                        "dataHoraFim": "2026-04-10T15:00:00",
                                        "status": "CANCELADO",
                                        "valorTotal": 120.00,
                                        "valorSinal": 0.00,
                                        "quantidadeParcelas": 1,
                                        "motivoCancelamento": "Cliente solicitou cancelamento",
                                        "observacoes": "Primeira sessão"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agendamento não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_404_AGENDAMENTO)
                    )
            )
    })
    public ResponseEntity<AgendamentoResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoResponseDTO.fromEntity(service.updateStatus(id, StatusAgendamento.CANCELADO)));
    }

    @PutMapping("/{id}/nao-compareceu")
    @Operation(summary = "Registrar não comparecimento", description = "Muda o status do agendamento para NAO_COMPARECEU")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Status atualizado para NAO_COMPARECEU",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 1,
                                        "cliente": { "id": 1, "nome": "Maria Silva" },
                                        "servico": {
                                            "id": 1,
                                            "nome": "Drenagem Linfática",
                                            "descricao": "Drenagem linfática corporal",
                                            "preco": 120.00,
                                            "duracaoMinutos": 60,
                                            "ativo": true
                                        },
                                        "funcionario": { "id": 1, "nome": "Ana Souza" },
                                        "dataHoraInicio": "2026-04-10T14:00:00",
                                        "dataHoraFim": "2026-04-10T15:00:00",
                                        "status": "NAO_COMPARECEU",
                                        "valorTotal": 120.00,
                                        "valorSinal": 0.00,
                                        "quantidadeParcelas": 1,
                                        "motivoCancelamento": null,
                                        "observacoes": "Primeira sessão"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agendamento não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_404_AGENDAMENTO)
                    )
            )
    })
    public ResponseEntity<AgendamentoResponseDTO> naoCompareceu(@PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoResponseDTO.fromEntity(service.updateStatus(id, StatusAgendamento.NAO_COMPARECEU)));
    }

    @PutMapping("/{id}/encerrar")
    @Operation(summary = "Encerrar agendamento", description = "Muda o status do agendamento para CONCLUIDO")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamento encerrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 1,
                                        "cliente": { "id": 1, "nome": "Maria Silva" },
                                        "servico": {
                                            "id": 1,
                                            "nome": "Drenagem Linfática",
                                            "descricao": "Drenagem linfática corporal",
                                            "preco": 120.00,
                                            "duracaoMinutos": 60,
                                            "ativo": true
                                        },
                                        "funcionario": { "id": 1, "nome": "Ana Souza" },
                                        "dataHoraInicio": "2026-04-10T14:00:00",
                                        "dataHoraFim": "2026-04-10T15:00:00",
                                        "status": "CONCLUIDO",
                                        "valorTotal": 120.00,
                                        "valorSinal": 0.00,
                                        "quantidadeParcelas": 1,
                                        "motivoCancelamento": null,
                                        "observacoes": "Primeira sessão"
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Agendamento não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_404_AGENDAMENTO)
                    )
            )
    })
    public ResponseEntity<AgendamentoResponseDTO> encerrar(@PathVariable Long id) {
        return ResponseEntity.ok(AgendamentoResponseDTO.fromEntity(service.updateStatus(id, StatusAgendamento.CONCLUIDO)));
    }

    @GetMapping("/hoje")
    @Operation(summary = "Agendamentos de hoje", description = "Retorna todos os agendamentos do dia atual")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamentos de hoje retornados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_LISTA_AGENDAMENTOS)
                    )
            )
    })
    public ResponseEntity<List<AgendamentoResponseDTO>> hoje() {
        List<AgendamentoResponseDTO> dtos = service.findByData(LocalDate.now()).stream()
                .map(AgendamentoResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/hoje/resumo")
    @Operation(summary = "Resumo do dia", description = "Retorna um resumo dos agendamentos do dia atual")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Resumo do dia retornado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_LISTA_AGENDAMENTOS)
                    )
            )
    })
    public ResponseEntity<List<AgendamentoResponseDTO>> resumoDia() {
        List<AgendamentoResponseDTO> dtos = service.findByData(LocalDate.now()).stream()
                .map(AgendamentoResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/periodo")
    @Operation(
            summary = "Agendamentos por período",
            description = "Retorna agendamentos entre as datas informadas. Formato das datas: yyyy-MM-dd"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamentos do período retornados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_LISTA_AGENDAMENTOS)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Formato de data inválido",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 400,
                                        "error": "BAD REQUEST",
                                        "message": "Formato de data inválido. Use o formato: yyyy-MM-dd"
                                    }
                                    """)
                    )
            )
    })
    public ResponseEntity<List<AgendamentoResponseDTO>> periodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<AgendamentoResponseDTO> dtos = service.findByPeriodo(inicio, fim).stream()
                .map(AgendamentoResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/proximos")
    @Operation(
            summary = "Próximos agendamentos",
            description = "Retorna os agendamentos dos próximos N dias a partir de hoje. Padrão: 7 dias"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Próximos agendamentos retornados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_LISTA_AGENDAMENTOS)
                    )
            )
    })
    public ResponseEntity<List<AgendamentoResponseDTO>> proximos(@RequestParam(defaultValue = "7") int dias) {
        LocalDate hoje = LocalDate.now();
        List<AgendamentoResponseDTO> dtos = service.findByPeriodo(hoje, hoje.plusDays(dias)).stream()
                .map(AgendamentoResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/status")
    @Operation(
            summary = "Agendamentos por status",
            description = "Filtra agendamentos por status. Valores disponíveis: PENDENTE, CONFIRMADO, CANCELADO, REALIZADO, CONCLUIDO, NAO_COMPARECEU"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamentos filtrados por status retornados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_LISTA_AGENDAMENTOS)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Status inválido",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                        "status": 400,
                                        "error": "BAD REQUEST",
                                        "message": "Status inválido. Valores aceitos: PENDENTE, CONFIRMADO, CANCELADO, REALIZADO, CONCLUIDO, NAO_COMPARECEU"
                                    }
                                    """)
                    )
            )
    })
    public ResponseEntity<List<AgendamentoResponseDTO>> porStatus(@RequestParam StatusAgendamento status) {
        List<AgendamentoResponseDTO> dtos = service.findByStatus(status).stream()
                .map(AgendamentoResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/funcionario/{id}")
    @Operation(summary = "Agendamentos por funcionário", description = "Retorna todos os agendamentos de um funcionário específico")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamentos do funcionário retornados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_LISTA_AGENDAMENTOS)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Funcionário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_404_FUNCIONARIO)
                    )
            )
    })
    public ResponseEntity<List<AgendamentoResponseDTO>> porFuncionario(@PathVariable Long id) {
        List<AgendamentoResponseDTO> dtos = service.findByFuncionarioId(id).stream()
                .map(AgendamentoResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/cliente/{id}")
    @Operation(summary = "Agendamentos por cliente", description = "Retorna todos os agendamentos de um cliente específico")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamentos do cliente retornados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_LISTA_AGENDAMENTOS)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_404_CLIENTE)
                    )
            )
    })
    public ResponseEntity<List<AgendamentoResponseDTO>> porCliente(@PathVariable Long id) {
        List<AgendamentoResponseDTO> dtos = service.findByClienteId(id).stream()
                .map(AgendamentoResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    //Exemplos reutilizaveis
    private static final String EXEMPLO_AGENDAMENTO = """
            {
                "id": 1,
                "cliente": {
                    "id": 1,
                    "nome": "Maria Silva"
                },
                "servico": {
                    "id": 1,
                    "nome": "Drenagem Linfática",
                    "descricao": "Drenagem linfática corporal",
                    "preco": 120.00,
                    "duracaoMinutos": 60,
                    "ativo": true
                },
                "funcionario": {
                    "id": 1,
                    "nome": "Ana Souza"
                },
                "dataHoraInicio": "2026-04-10T14:00:00",
                "dataHoraFim": "2026-04-10T15:00:00",
                "status": "PENDENTE",
                "valorTotal": 120.00,
                "valorSinal": 0.00,
                "quantidadeParcelas": 1,
                "motivoCancelamento": null,
                "observacoes": "Primeira sessão"
            }
            """;

    private static final String EXEMPLO_LISTA_AGENDAMENTOS = """
            [
                {
                    "id": 1,
                    "cliente": {
                        "id": 1,
                        "nome": "Maria Silva"
                    },
                    "servico": {
                        "id": 1,
                        "nome": "Drenagem Linfática",
                        "descricao": "Drenagem linfática corporal",
                        "preco": 120.00,
                        "duracaoMinutos": 60,
                        "ativo": true
                    },
                    "funcionario": {
                        "id": 1,
                        "nome": "Ana Souza"
                    },
                    "dataHoraInicio": "2026-04-10T14:00:00",
                    "dataHoraFim": "2026-04-10T15:00:00",
                    "status": "PENDENTE",
                    "valorTotal": 120.00,
                    "valorSinal": 0.00,
                    "quantidadeParcelas": 1,
                    "motivoCancelamento": null,
                    "observacoes": "Primeira sessão"
                }
            ]
            """;

    private static final String EXEMPLO_400 = """
            {
                "status": 400,
                "error": "BAD REQUEST",
                "message": "Erro na validação dos campos",
                "errors": [
                    "clienteId: Cliente ID é obrigatório",
                    "servicoId: Serviço ID é obrigatório",
                    "funcionarioId: Funcionário ID é obrigatório",
                    "dataHoraInicio: Data/hora de início é obrigatória",
                    "dataHoraFim: Data/hora de fim é obrigatória"
                ]
            }
            """;

    private static final String EXEMPLO_404_AGENDAMENTO = """
            {
                "status": 404,
                "error": "NOT FOUND",
                "message": "Agendamento não encontrado"
            }
            """;

    private static final String EXEMPLO_404_CLIENTE = """
            {
                "status": 404,
                "error": "NOT FOUND",
                "message": "Cliente não encontrado"
            }
            """;

    private static final String EXEMPLO_404_FUNCIONARIO = """
            {
                "status": 404,
                "error": "NOT FOUND",
                "message": "Funcionário não encontrado"
            }
            """;
}