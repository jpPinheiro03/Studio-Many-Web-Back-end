package com.studio.core.dominio.agendamento.controller;

import com.studio.core.dominio.agendamento.dto.AgendamentoRecorrenteRequestDTO;
import com.studio.core.dominio.agendamento.dto.AgendamentoRequestDTO;
import com.studio.core.dominio.agendamento.dto.AgendamentoResponseDTO;
import com.studio.core.dominio.agendamento.dto.CancelamentoPremiumRequestDTO;
import com.studio.core.dominio.agendamento.dto.HistoricoNoShowDTO;
import com.studio.core.dominio.agendamento.dto.ReagendamentoRequestDTO;
import com.studio.core.dominio.agendamento.entity.Agendamento;
import com.studio.core.dominio.agendamento.entity.StatusAgendamento;
import com.studio.core.dominio.agendamento.mapper.AgendamentoMapper;
import com.studio.core.service.AgendamentoPremiumService;
import com.studio.core.service.AgendamentoService;
import com.studio.core.service.provider.FileStorageProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agendamentos")
@Tag(name = "Agendamentos", description = "Endpoints para gestão de agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;

    @Autowired
    private AgendamentoPremiumService premiumService;

    @Autowired
    private FileStorageProvider fileStorageProvider;

    @Autowired
    private AgendamentoMapper agendamentoMapper;

    @GetMapping
    @Operation(
            summary = "Listar agendamentos (com paginação opcional)",
            description = "Retorna todos os agendamentos com suporte a paginação. Se page não for informado, retorna todos."
    )
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
    public ResponseEntity<List<AgendamentoResponseDTO>> listar(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        List<Agendamento> agendamentos;
        if (page != null) {
            Page<Agendamento> pageResult = service.findPaginated(page, size);
            agendamentos = pageResult.getContent();
        } else {
            agendamentos = service.findAll();
        }
        List<AgendamentoResponseDTO> dtos = agendamentos.stream()
                .map(agendamentoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar agendamento por ID",
            description = "Retorna os detalhes de um agendamento específico pelo seu identificador"
    )
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
        return ResponseEntity.ok(agendamentoMapper.toResponse(service.findById(id)));
    }

    @PostMapping
    @Operation(
            summary = "Criar agendamento",
            description = "Cria um novo agendamento com os dados informados"
    )
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
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito de horário - horário já ocupado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_409_CONFLITO_HORARIO)
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
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoMapper.toResponse(created));
    }

    @PutMapping("/{id}/status")
    @Operation(
            summary = "Atualizar status do agendamento",
            description = "Atualiza o status de um agendamento. Status disponíveis: PENDENTE, AGUARDANDO_CONFIRMACAO, CONFIRMADO, EM_ATENDIMENTO, CONCLUIDO, CANCELADO, NAO_COMPARECEU, REAGENDADO"
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
                    description = "Status inválido ou transição não permitida",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_400_STATUS)
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
        return ResponseEntity.ok(agendamentoMapper.toResponse(service.updateStatus(id, status)));
    }

    @PutMapping("/{id}/enviar-comprovante")
    @Operation(
            summary = "Cliente envia comprovante de sinal",
            description = "Cliente envia comprovante de sinal via texto (URL ou caminho). Altera status de PENDENTE para AGUARDANDO_CONFIRMACAO"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Comprovante enviado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_AGENDAMENTO)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Agendamento não está no status PENDENTE",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_400_STATUS_AGUARDANDO)
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
    public ResponseEntity<AgendamentoResponseDTO> enviarComprovante(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        String comprovante = body != null ? body.get("comprovanteSinal") : null;
        return ResponseEntity.ok(agendamentoMapper.toResponse(service.enviarComprovanteSinal(id, comprovante)));
    }

    @PostMapping("/{id}/upload-comprovante")
    @Operation(
            summary = "Upload de comprovante de sinal como arquivo",
            description = "Cliente envia comprovante de sinal em formato de arquivo. Altera status de PENDENTE para AGUARDANDO_CONFIRMACAO"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Arquivo enviado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_AGENDAMENTO)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Agendamento não está no status PENDENTE ou arquivo inválido",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_400_ARQUIVO)
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
    public ResponseEntity<AgendamentoResponseDTO> uploadComprovante(
            @PathVariable Long id,
            @RequestParam("arquivo") MultipartFile arquivo) throws Exception {
        String caminho = fileStorageProvider.store(arquivo, "comprovantes");
        return ResponseEntity.ok(agendamentoMapper.toResponse(service.enviarComprovanteSinal(id, caminho)));
    }

    @PutMapping("/{id}/confirmar-sinal")
    @Operation(
            summary = "Funcionário confirma recebimento do sinal",
            description = "Funcionário confirma o recebimento do sinal. Altera status de AGUARDANDO_CONFIRMACAO para CONFIRMADO"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sinal confirmado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_AGENDAMENTO)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Agendamento não está no status AGUARDANDO_CONFIRMACAO",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_400_STATUS_CONFIRMACAO)
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
    public ResponseEntity<AgendamentoResponseDTO> confirmarSinal(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoMapper.toResponse(service.confirmarSinal(id)));
    }

    @PutMapping("/{id}/registrar-chegada")
    @Operation(
            summary = "Registrar chegada do cliente",
            description = "Registra a chegada do cliente ao estabelecimento. Altera status de CONFIRMADO para EM_ATENDIMENTO e dispara notificação"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Chegada registrada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_REGISTRO_CHEGADA)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Agendamento não está no status CONFIRMADO",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_400_STATUS_CHEGADA)
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
    public ResponseEntity<Map<String, Object>> registrarChegada(@PathVariable Long id) {
        Agendamento agendamento = service.registrarChegada(id);
        return ResponseEntity.ok(Map.of(
                "agendamento", agendamentoMapper.toResponse(agendamento),
                "notificacao", Map.of(
                        "mensagem", "Cliente " + agendamento.getCliente().getNome() + " chegou ao estabelecimento",
                        "clienteId", agendamento.getCliente().getId(),
                        "funcionarioId", agendamento.getFuncionario().getId()
                )
        ));
    }

    @PutMapping("/{id}/finalizar")
    @Operation(
            summary = "Finalizar atendimento e pagamento",
            description = "Finaliza o atendimento e registra o pagamento. Altera status de EM_ATENDIMENTO para CONCLUIDO"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Atendimento finalizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_AGENDAMENTO_CONCLUIDO)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Agendamento não está no status EM_ATENDIMENTO",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_400_STATUS_FINALIZAR)
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
    public ResponseEntity<AgendamentoResponseDTO> finalizar(@PathVariable Long id) {
        return ResponseEntity.ok(agendamentoMapper.toResponse(service.finalizarAtendimento(id)));
    }

    @PutMapping("/{id}/cancelar")
    @Operation(
            summary = "Cancelar agendamento",
            description = "Cancela um agendamento com motivo opcional. Altera status para CANCELADO"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamento cancelado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_AGENDAMENTO_CANCELADO)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Agendamento não pode ser cancelado no status atual",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_400_CANCELAMENTO)
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
    public ResponseEntity<AgendamentoResponseDTO> cancelar(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        String motivo = body != null ? body.get("motivo") : null;
        return ResponseEntity.ok(agendamentoMapper.toResponse(service.cancelar(id, motivo, null)));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar agendamento",
            description = "Atualiza os dados de um agendamento existente"
    )
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
        return ResponseEntity.ok(agendamentoMapper.toResponse(service.update(id, entity)));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir agendamento",
            description = "Remove um agendamento do sistema"
    )
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

    @GetMapping("/hoje")
    @Operation(
            summary = "Agendamentos de hoje",
            description = "Retorna todos os agendamentos do dia atual"
    )
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
                .map(agendamentoMapper::toResponse)
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
                            examples = @ExampleObject(value = EXEMPLO_400_DATA)
                    )
            )
    })
    public ResponseEntity<List<AgendamentoResponseDTO>> periodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<AgendamentoResponseDTO> dtos = service.findByPeriodo(inicio, fim).stream()
                .map(agendamentoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/status")
    @Operation(
            summary = "Agendamentos por status",
            description = "Filtra agendamentos por status. Valores disponíveis: PENDENTE, AGUARDANDO_CONFIRMACAO, CONFIRMADO, EM_ATENDIMENTO, CONCLUIDO, CANCELADO, NAO_COMPARECEU, REAGENDADO"
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
                            examples = @ExampleObject(value = EXEMPLO_400_STATUS_INVALIDO)
                    )
            )
    })
    public ResponseEntity<List<AgendamentoResponseDTO>> porStatus(@RequestParam StatusAgendamento status) {
        List<AgendamentoResponseDTO> dtos = service.findByStatus(status).stream()
                .map(agendamentoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/funcionario/{id}")
    @Operation(
            summary = "Agendamentos por funcionário",
            description = "Retorna todos os agendamentos de um funcionário específico"
    )
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
                .map(agendamentoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/cliente/{id}")
    @Operation(
            summary = "Agendamentos por cliente",
            description = "Retorna todos os agendamentos de um cliente específico"
    )
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
                .map(agendamentoMapper::toResponse)
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
                .map(agendamentoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/hoje/resumo")
    @Operation(
            summary = "Resumo do dia",
            description = "Retorna um resumo dos agendamentos do dia atual"
    )
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
                .map(agendamentoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}/nao-compareceu")
    @Operation(
            summary = "Registrar não comparecimento",
            description = "Registra que o cliente não compareceu ao agendamento. Altera status para NAO_COMPARECEU"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Status atualizado para NAO_COMPARECEU",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_AGENDAMENTO_NAO_COMPARECEU)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Agendamento não pode ser marcado como não compareceu no status atual",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_400_STATUS_NAO_COMPARECEU)
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
        return ResponseEntity.ok(agendamentoMapper.toResponse(service.naoCompareceu(id)));
    }

    @PutMapping("/{id}/reagendar")
    @Operation(
            summary = "Reagendar agendamento",
            description = "Cria um novo agendamento e marca o antigo como REAGENDADO"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Agendamento reagendado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_AGENDAMENTO)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida ou horário indisponível",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_400_REAGENDAMENTO)
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
    public ResponseEntity<AgendamentoResponseDTO> reagendar(
            @PathVariable Long id, @RequestBody ReagendamentoRequestDTO dto) {
        return ResponseEntity.ok(agendamentoMapper.toResponse(premiumService.reagendar(id, dto)));
    }

    @GetMapping("/{id}/taxa-cancelamento")
    @Operation(
            summary = "Calcular taxa de cancelamento",
            description = "Calcula a taxa de cancelamento baseada no horário do agendamento"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Taxa calculada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_TAXA_CANCELAMENTO)
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
    public ResponseEntity<Map<String, Object>> calcularTaxa(@PathVariable Long id) {
        BigDecimal taxa = premiumService.calcularTaxaCancelamento(id);
        Agendamento a = service.findById(id);
        long horas = java.time.Duration.between(java.time.LocalDateTime.now(), a.getDataHoraInicio()).toHours();
        return ResponseEntity.ok(Map.of(
                "agendamentoId", id,
                "taxa", taxa,
                "horasAntes", horas,
                "politica", Map.of(
                        "> 48h", "sem taxa",
                        "24-48h", "20%",
                        "12-24h", "50%",
                        "< 12h", "100%"
                )
        ));
    }

    @PutMapping("/{id}/cancelar-premium")
    @Operation(
            summary = "Cancelar com aplicação de taxa de cancelamento",
            description = "Cancela o agendamento aplicando a taxa de cancelamento conforme política definida"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cancelamento realizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_AGENDAMENTO_CANCELADO)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Agendamento não pode ser cancelado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_400_CANCELAMENTO_PREMIUM)
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
    public ResponseEntity<AgendamentoResponseDTO> cancelarPremium(
            @PathVariable Long id, @RequestBody CancelamentoPremiumRequestDTO dto) {
        return ResponseEntity.ok(agendamentoMapper.toResponse(premiumService.cancelarComTaxa(id, dto)));
    }

    @GetMapping("/no-shows/{clienteId}")
    @Operation(
            summary = "Histórico de no-shows do cliente",
            description = "Retorna o histórico de não comparecimentos do cliente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Histórico retornado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_HISTORICO_NO_SHOW)
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
    public ResponseEntity<HistoricoNoShowDTO> historicoNoShows(@PathVariable Long clienteId) {
        return ResponseEntity.ok(premiumService.historicoNoShows(clienteId));
    }

    @GetMapping("/no-shows/{clienteId}/bloqueado")
    @Operation(
            summary = "Verificar se cliente está bloqueado por no-shows",
            description = "Verifica se o cliente está bloqueado devido a excesso de não comparecimentos"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Status de bloqueio retornado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_BLOQUEADO)
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
    public ResponseEntity<Map<String, Boolean>> clienteBloqueado(@PathVariable Long clienteId) {
        return ResponseEntity.ok(Map.of("bloqueado", premiumService.clienteBloqueadoPorNoShows(clienteId)));
    }

    @GetMapping("/sugestoes")
    @Operation(
            summary = "Sugerir horários alternativos",
            description = "Sugere horários alternativos próximos à data desejada"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Sugestões retornadas com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_SUGESTOES)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parâmetros inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_400_SUGESTOES)
                    )
            )
    })
    public ResponseEntity<List<LocalTime>> sugestoes(
            @RequestParam Long servicoId,
            @RequestParam Long funcionarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataDesejada,
            @RequestParam(defaultValue = "7") int diasRange) {
        return ResponseEntity.ok(premiumService.sugerirHorariosProximos(servicoId, funcionarioId, dataDesejada, diasRange));
    }

    @PostMapping("/recorrente")
    @Operation(
            summary = "Criar agendamentos recorrentes",
            description = "Cria uma série de agendamentos recorrentes conforme periodicidade informada"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Agendamentos recorrentes criados com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_LISTA_AGENDAMENTOS)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Requisição inválida",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = EXEMPLO_400_RECORRENTE)
                    )
            )
    })
    public ResponseEntity<List<AgendamentoResponseDTO>> criarRecorrente(@RequestBody AgendamentoRecorrenteRequestDTO dto) {
        List<AgendamentoResponseDTO> dtos = premiumService.criarRecorrente(dto).stream()
                .map(agendamentoMapper::toResponse).toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(dtos);
    }



    // ---------- Exemplos reutilizáveis para documentação Swagger - João
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

    private static final String EXEMPLO_AGENDAMENTO_CONCLUIDO = """
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
                "status": "CONCLUIDO",
                "valorTotal": 120.00,
                "valorSinal": 0.00,
                "quantidadeParcelas": 1,
                "motivoCancelamento": null,
                "observacoes": "Primeira sessão"
            }
            """;

    private static final String EXEMPLO_AGENDAMENTO_CANCELADO = """
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
                "status": "CANCELADO",
                "valorTotal": 120.00,
                "valorSinal": 0.00,
                "quantidadeParcelas": 1,
                "motivoCancelamento": "Cliente solicitou cancelamento",
                "observacoes": "Primeira sessão"
            }
            """;

    private static final String EXEMPLO_AGENDAMENTO_NAO_COMPARECEU = """
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
                "status": "NAO_COMPARECEU",
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

    private static final String EXEMPLO_REGISTRO_CHEGADA = """
            {
                "agendamento": {
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
                    "status": "EM_ATENDIMENTO",
                    "valorTotal": 120.00,
                    "valorSinal": 0.00,
                    "quantidadeParcelas": 1,
                    "motivoCancelamento": null,
                    "observacoes": "Primeira sessão"
                },
                "notificacao": {
                    "mensagem": "Cliente Maria Silva chegou ao estabelecimento",
                    "clienteId": 1,
                    "funcionarioId": 1
                }
            }
            """;

    private static final String EXEMPLO_TAXA_CANCELAMENTO = """
            {
                "agendamentoId": 1,
                "taxa": 60.00,
                "horasAntes": 36,
                "politica": {
                    "> 48h": "sem taxa",
                    "24-48h": "20%",
                    "12-24h": "50%",
                    "< 12h": "100%"
                }
            }
            """;

    private static final String EXEMPLO_HISTORICO_NO_SHOW = """
            {
                "clienteId": 1,
                "clienteNome": "Maria Silva",
                "totalNoShows": 2,
                "bloqueado": false,
                "historico": [
                    {
                        "agendamentoId": 1,
                        "dataAgendamento": "2026-04-10T14:00:00",
                        "servico": "Drenagem Linfática"
                    }
                ]
            }
            """;

    private static final String EXEMPLO_BLOQUEADO = """
            {
                "bloqueado": false
            }
            """;

    private static final String EXEMPLO_SUGESTOES = """
            [
                "14:00:00",
                "14:30:00",
                "15:00:00",
                "15:30:00"
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

    private static final String EXEMPLO_400_DATA = """
            {
                "status": 400,
                "error": "BAD REQUEST",
                "message": "Formato de data inválido. Use o formato: yyyy-MM-dd"
            }
            """;

    private static final String EXEMPLO_400_STATUS = """
            {
                "status": 400,
                "error": "BAD REQUEST",
                "message": "Status inválido. Valores aceitos: PENDENTE, AGUARDANDO_CONFIRMACAO, CONFIRMADO, EM_ATENDIMENTO, CONCLUIDO, CANCELADO, NAO_COMPARECEU, REAGENDADO"
            }
            """;

    private static final String EXEMPLO_400_STATUS_INVALIDO = """
            {
                "status": 400,
                "error": "BAD REQUEST",
                "message": "Status inválido. Valores aceitos: PENDENTE, AGUARDANDO_CONFIRMACAO, CONFIRMADO, EM_ATENDIMENTO, CONCLUIDO, CANCELADO, NAO_COMPARECEU, REAGENDADO"
            }
            """;

    private static final String EXEMPLO_400_STATUS_AGUARDANDO = """
            {
                "status": 400,
                "error": "BAD REQUEST",
                "message": "Agendamento deve estar no status PENDENTE para enviar comprovante"
            }
            """;

    private static final String EXEMPLO_400_STATUS_CONFIRMACAO = """
            {
                "status": 400,
                "error": "BAD REQUEST",
                "message": "Agendamento deve estar no status AGUARDANDO_CONFIRMACAO para confirmar sinal"
            }
            """;

    private static final String EXEMPLO_400_STATUS_CHEGADA = """
            {
                "status": 400,
                "error": "BAD REQUEST",
                "message": "Agendamento deve estar no status CONFIRMADO para registrar chegada"
            }
            """;

    private static final String EXEMPLO_400_STATUS_FINALIZAR = """
            {
                "status": 400,
                "error": "BAD REQUEST",
                "message": "Agendamento deve estar no status EM_ATENDIMENTO para finalizar"
            }
            """;

    private static final String EXEMPLO_400_STATUS_NAO_COMPARECEU = """
            {
                "status": 400,
                "error": "BAD REQUEST",
                "message": "Agendamento deve estar no status CONFIRMADO ou EM_ATENDIMENTO para marcar como não compareceu"
            }
            """;

    private static final String EXEMPLO_400_CANCELAMENTO = """
            {
                "status": 400,
                "error": "BAD REQUEST",
                "message": "Agendamento não pode ser cancelado no status atual"
            }
            """;

    private static final String EXEMPLO_400_CANCELAMENTO_PREMIUM = """
            {
                "status": 400,
                "error": "BAD REQUEST",
                "message": "Agendamento não pode ser cancelado no status atual"
            }
            """;

    private static final String EXEMPLO_400_REAGENDAMENTO = """
            {
                "status": 400,
                "error": "BAD REQUEST",
                "message": "Horário indisponível para reagendamento"
            }
            """;

    private static final String EXEMPLO_400_RECORRENTE = """
            {
                "status": 400,
                "error": "BAD REQUEST",
                "message": "Periodicidade inválida. Valores aceitos: DIARIA, SEMANAL, QUINZENAL, MENSAL"
            }
            """;

    private static final String EXEMPLO_400_SUGESTOES = """
            {
                "status": 400,
                "error": "BAD REQUEST",
                "message": "Serviço ou funcionário não encontrado"
            }
            """;

    private static final String EXEMPLO_400_ARQUIVO = """
            {
                "status": 400,
                "error": "BAD REQUEST",
                "message": "Arquivo inválido ou não enviado"
            }
            """;

    private static final String EXEMPLO_409_CONFLITO_HORARIO = """
            {
                "status": 409,
                "error": "CONFLICT",
                "message": "Horário já está ocupado para este funcionário"
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