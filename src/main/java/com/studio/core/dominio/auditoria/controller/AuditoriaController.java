package com.studio.core.dominio.auditoria.controller;

import com.studio.core.dominio.auditoria.entity.Auditoria;
import com.studio.core.service.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@Tag(name = "Auditoria", description = "Endpoints para consulta de logs de auditoria do sistema")
public class AuditoriaController {

    @Autowired
    private AuditoriaService service;

    @GetMapping("/{entidade}/{id}")
    @Operation(
            summary = "Consultar auditoria por entidade e ID do registro",
            description = "Retorna o histórico de alterações de um registro específico, ordenado da mais recente para a mais antiga"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Histórico de auditoria retornado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_LISTA_AUDITORIA)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parâmetros inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_400)
                    )
            )
    })
    public ResponseEntity<List<Auditoria>> porEntidadeEId(
            @PathVariable String entidade,
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findByEntidadeAndId(entidade, id));
    }

    @GetMapping("/entidade/{entidade}")
    @Operation(
            summary = "Consultar auditoria por tipo de entidade",
            description = "Retorna todas as alterações realizadas em registros de uma determinada entidade (ex: Cliente, Agendamento, Usuario)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Auditoria da entidade retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_LISTA_AUDITORIA)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Nome da entidade inválido",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_400_ENTIDADE)
                    )
            )
    })
    public ResponseEntity<List<Auditoria>> porEntidade(@PathVariable String entidade) {
        return ResponseEntity.ok(service.findByEntidade(entidade));
    }

    @GetMapping("/periodo")
    @Operation(
            summary = "Consultar auditoria por período",
            description = "Retorna todas as alterações realizadas em um intervalo de datas. Formato: yyyy-MM-ddTHH:mm:ss"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Auditoria do período retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_LISTA_AUDITORIA)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Formato de data inválido ou data inicial maior que data final",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_400_DATA)
                    )
            )
    })
    public ResponseEntity<List<Auditoria>> periodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(service.findByPeriodo(inicio, fim));
    }

    // ------ Exemplos reutilizáveis para documentação Swagger - João
    private static final String EXEMPLO_AUDITORIA = """
            {
                "id": 1,
                "entidade": "Agendamento",
                "entidadeId": 10,
                "acao": "UPDATE",
                "dadosAnteriores": "{\\"status\\":\\"PENDENTE\\",\\"valorTotal\\":120.00}",
                "dadosNovos": "{\\"status\\":\\"CONFIRMADO\\",\\"valorTotal\\":120.00}",
                "usuario": {
                    "id": 1,
                    "nome": "Ana Souza",
                    "email": "ana@studio.com"
                },
                "dataAcao": "2026-04-10T14:30:00"
            }
            """;

    private static final String EXEMPLO_LISTA_AUDITORIA = """
            [
                {
                    "id": 1,
                    "entidade": "Agendamento",
                    "entidadeId": 10,
                    "acao": "UPDATE",
                    "dadosAnteriores": "{\\"status\\":\\"PENDENTE\\",\\"valorTotal\\":120.00}",
                    "dadosNovos": "{\\"status\\":\\"CONFIRMADO\\",\\"valorTotal\\":120.00}",
                    "usuario": {
                        "id": 1,
                        "nome": "Ana Souza",
                        "email": "ana@studio.com"
                    },
                    "dataAcao": "2026-04-10T14:30:00"
                },
                {
                    "id": 2,
                    "entidade": "Agendamento",
                    "entidadeId": 10,
                    "acao": "CREATE",
                    "dadosAnteriores": null,
                    "dadosNovos": "{\\"status\\":\\"PENDENTE\\",\\"clienteId\\":5,\\"servicoId\\":3,\\"funcionarioId\\":2,\\"valorTotal\\":120.00}",
                    "usuario": {
                        "id": 3,
                        "nome": "Carlos Lima",
                        "email": "carlos@studio.com"
                    },
                    "dataAcao": "2026-04-09T10:15:00"
                },
                {
                    "id": 3,
                    "entidade": "Cliente",
                    "entidadeId": 5,
                    "acao": "UPDATE",
                    "dadosAnteriores": "{\\"nome\\":\\"Maria Silva\\",\\"telefone\\":\\"11999999999\\"}",
                    "dadosNovos": "{\\"nome\\":\\"Maria Souza Silva\\",\\"telefone\\":\\"11988888888\\"}",
                    "usuario": {
                        "id": 1,
                        "nome": "Ana Souza",
                        "email": "ana@studio.com"
                    },
                    "dataAcao": "2026-04-08T09:45:00"
                }
            ]
            """;

    private static final String EXEMPLO_400 = """
            {
                "status": 400,
                "error": "BAD REQUEST",
                "message": "Parâmetros inválidos. Entidade e ID do registro são obrigatórios"
            }
            """;

    private static final String EXEMPLO_400_ENTIDADE = """
            {
                "status": 400,
                "error": "BAD REQUEST",
                "message": "Nome da entidade inválido. Entidades disponíveis: Cliente, Agendamento, Usuario, Servico, Funcionario"
            }
            """;

    private static final String EXEMPLO_400_DATA = """
            {
                "status": 400,
                "error": "BAD REQUEST",
                "message": "Formato de data inválido. Use o formato: yyyy-MM-ddTHH:mm:ss. Exemplo: 2026-04-10T14:30:00"
            }
            """;
}