package com.studio.core.dominio.parcela.controller;

import com.studio.core.dominio.parcela.dto.ParcelaRequestDTO;
import com.studio.core.dominio.parcela.dto.ParcelaResponseDTO;
import com.studio.core.service.ParcelaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/parcelas")
@Tag(name = "Parcelas", description = "Endpoints para gestão de parcelas")
public class ParcelaController {
    
    @Autowired
    private ParcelaService service;
    
    @GetMapping
    @Operation(summary = "Listar parcelas")
    @ApiResponses(
            @ApiResponse(responseCode = "200",
                    description = "Parcela encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                [
                                    {
                                        "id": 1,
                                        "numero": 3,
                                        "valor": 300,
                                        "dataVencimento": "2026-03-30",
                                        "dataPagamento": "2026-03-28",
                                        "status": "QUITADA"
                                    }
                                ]
                            """)
                    )
            )
    )
    public ResponseEntity<List<ParcelaResponseDTO>> listar() {
        return ResponseEntity.ok(service.findAll());
    }
    
    @GetMapping("/agendamento/{id}")
    @Operation(summary = "Listar parcelas por ID do agendamento")
    @ApiResponses(
            @ApiResponse(responseCode = "200",
                    description = "Parcela encontrada por ID",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                [
                                    {
                                        "id": 1,
                                        "numero": 3,
                                        "valor": 300,
                                        "dataVencimento": "2026-03-30",
                                        "dataPagamento": "2026-03-28",
                                        "status": "QUITADA"
                                    }
                                ]
                            """)
                    )
            )
    )
    public ResponseEntity<List<ParcelaResponseDTO>> porAgendamento(@PathVariable Long id) {
        return ResponseEntity.ok(service.findByAgendamentoId(id));
    }
    
    @GetMapping("/pendentes")
    @Operation(summary = "Listar parcelas pendentes")
    @ApiResponses(
            @ApiResponse(responseCode = "200",
                    description = "Parcelas pendentes",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                [
                                    {
                                        "id": 1,
                                        "numero": 3,
                                        "valor": 300,
                                        "dataVencimento": "2026-03-30",
                                        "dataPagamento": "2026-03-28",
                                        "status": "PENDENTE"
                                    }
                                ]
                            """)
                    )
            )
    )
    public ResponseEntity<List<ParcelaResponseDTO>> pendentes() {
        return ResponseEntity.ok(service.findPendentes());
    }
    
    @GetMapping("/vencidas")
    @Operation(summary = "Listar parcelas vencidas")
    @ApiResponses(
            @ApiResponse(responseCode = "200",
                    description = "Parcelas vencidas",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                [
                                    {
                                        "id": 1,
                                        "numero": 3,
                                        "valor": 300,
                                        "dataVencimento": "2026-03-30",
                                        "dataPagamento": "2026-03-28",
                                        "status": "VENCIDA"
                                    }
                                ]
                            """)
                    )
            )
    )
    public ResponseEntity<List<ParcelaResponseDTO>> vencidas() {
        return ResponseEntity.ok(service.findVencidas());
    }
    
    @GetMapping("/vencer-proximo")
    @Operation(summary = "Listar parcelas próximas ao vencimento")
    @ApiResponses(
            @ApiResponse(responseCode = "200",
                    description = "Parcelas próximas ao vencimento",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                [
                                    {
                                        "id": 1,
                                        "numero": 3,
                                        "valor": 300,
                                        "dataVencimento": "2026-03-30",
                                        "dataPagamento": "2026-03-28",
                                        "status": "PENDENTE"
                                    }
                                ]
                            """)
                    )
            )
    )
    public ResponseEntity<List<ParcelaResponseDTO>> aVencer(@RequestParam(defaultValue = "30") int dias) {
        return ResponseEntity.ok(service.findAVencer(dias));
    }

    @PostMapping("/gerar/{agendamentoId}")
    @Operation(summary = "Gerar parcelas")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Parcela gerada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                [
                                    {
                                        "id": 1,
                                        "numero": 3,
                                        "valor": 300,
                                        "dataVencimento": "2026-03-30",
                                        "dataPagamento": "2026-03-28",
                                        "status": "QUITADA"
                                    }
                                ]
                            """)
                    )

            ),
            @ApiResponse(responseCode = "404",
                    description = "Agendamento não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "status": 404,
                                    "error": "NOT FOUND",
                                    "message": "Agendamento não encontrado"
                                }
                            """)
                    )
            )
    })
    public ResponseEntity<List<ParcelaResponseDTO>> gerar(
            @PathVariable Long agendamentoId,
            @RequestBody ParcelaRequestDTO dto) {
        List<ParcelaResponseDTO> parcelas = service.gerarParcelas(agendamentoId, dto.getQuantidade(), dto.getDataPrimeira());

        return ResponseEntity.status(HttpStatus.CREATED).body(parcelas);
    }
    
    @PutMapping("/{id}/quitar")
    @Operation(summary = "Quitar parcela por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Parcela quitada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "id": 1,
                                    "numero": 3,
                                    "valor": 300,
                                    "dataVencimento": "2026-03-30",
                                    "dataPagamento": "2026-03-28",
                                    "status": "QUITADA"
                                }
                            """)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Parcela não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "status": 404,
                                    "error": "NOT FOUND",
                                    "message": "Parcela não encontrada"
                                }
                            """)
                    )
            )
    })
    public ResponseEntity<ParcelaResponseDTO> quitar(@PathVariable Long id) {
        return ResponseEntity.ok(service.quitar(id));
    }
    
    @PutMapping("/agendamento/{id}/quitar-todas")
    @Operation(summary = "Quitar todas as parcelas por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Parcelas quitadas",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                [
                                    {
                                        "id": 1,
                                        "numero": 3,
                                        "valor": 300,
                                        "dataVencimento": "2026-03-30",
                                        "dataPagamento": "2026-03-28",
                                        "status": "QUITADA"
                                    }
                                ]
                            """)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Agendamento não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "status": 404,
                                    "error": "NOT FOUND",
                                    "message": "Agendamento não encontrado"
                                }
                            """)
                    )
            )
    })
    public ResponseEntity<List<ParcelaResponseDTO>> quitarTodas(@PathVariable Long id) {
        return ResponseEntity.ok(service.quitarTodas(id));
    }
}
