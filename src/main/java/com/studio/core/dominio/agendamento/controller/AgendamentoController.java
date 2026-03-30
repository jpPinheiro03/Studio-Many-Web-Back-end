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

@RestController // Esta classe aceita requisições HTTP e retorna JSON
@RequestMapping("/api/agendamentos") // URL base para todos os endpoints desta classe
@Tag(name = "Agendamentos", description = "Endpoints para gestão de agendamentos") // Swagger: agrupa endpoints por categoria
public class AgendamentoController {

    @Autowired // Injeta dependência automaticamente (injeção de dependência)
    private AgendamentoService service;

    @Autowired // Injeta dependência automaticamente (injeção de dependência)
    private AgendamentoPremiumService premiumService;

    @Autowired // Injeta dependência automaticamente (injeção de dependência)
    private FileStorageProvider fileStorageProvider;

    @Autowired // Injeta dependência automaticamente (injeção de dependência)
    private AgendamentoMapper agendamentoMapper;

    @GetMapping // Endpoint que aceita requisições GET
    @Operation(summary = "Listar agendamentos (com paginação opcional)") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<List<AgendamentoResponseDTO>> listar(
            @RequestParam(required = false) Integer page, // Recebe o valor do parâmetro da URL (?param=valor)
            @RequestParam(required = false, defaultValue = "20") Integer size) { // Recebe o valor do parâmetro com valor padrão
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
    
    @GetMapping("/{id}") // Endpoint que aceita requisições GET com parâmetro na URL
    public ResponseEntity<AgendamentoResponseDTO> buscarPorId(@PathVariable Long id) { // Recebe o valor da URL (ex: /agendamentos/{id})
        return ResponseEntity.ok(agendamentoMapper.toResponse(service.findById(id)));
    }
    
    @PostMapping // Endpoint que aceita requisições POST (criar)
    @Operation(summary = "Criar agendamento") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<AgendamentoResponseDTO> criar(@Valid @RequestBody AgendamentoRequestDTO dto) { // Valida os campos e recebe os dados do corpo da requisição HTTP (JSON)
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
    
    @PutMapping("/{id}/status") // Endpoint que aceita requisições PUT (atualizar) com parâmetro na URL
    @Operation(summary = "Atualizar status") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<AgendamentoResponseDTO> atualizarStatus(
            @PathVariable Long id, // Recebe o valor da URL (ex: /agendamentos/{id}/status)
            @RequestParam StatusAgendamento status) { // Recebe o valor do parâmetro da URL (?param=valor)
        return ResponseEntity.ok(agendamentoMapper.toResponse(service.updateStatus(id, status)));
    }
    
    @PutMapping("/{id}/enviar-comprovante") // Endpoint que aceita requisições PUT (atualizar) com parâmetro na URL
    @Operation(summary = "Cliente envia comprovante de sinal (PENDENTE → AGUARDANDO_CONFIRMACAO)") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<AgendamentoResponseDTO> enviarComprovante(
            @PathVariable Long id, // Recebe o valor da URL (ex: /agendamentos/{id}/enviar-comprovante)
            @RequestBody(required = false) Map<String, String> body) { // Recebe os dados do corpo da requisição HTTP (opcional)
        String comprovante = body != null ? body.get("comprovanteSinal") : null;
        return ResponseEntity.ok(agendamentoMapper.toResponse(service.enviarComprovanteSinal(id, comprovante)));
    }

    @PostMapping("/{id}/upload-comprovante") // Endpoint que aceita requisições POST (criar) com parâmetro na URL
    @Operation(summary = "Upload de comprovante de sinal como arquivo (PENDENTE → AGUARDANDO_CONFIRMACAO)") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<AgendamentoResponseDTO> uploadComprovante(
            @PathVariable Long id, // Recebe o valor da URL (ex: /agendamentos/{id}/upload-comprovante)
            @RequestParam("arquivo") MultipartFile arquivo) throws Exception { // Recebe o arquivo do parâmetro da URL
        String caminho = fileStorageProvider.store(arquivo, "comprovantes");
        return ResponseEntity.ok(agendamentoMapper.toResponse(service.enviarComprovanteSinal(id, caminho)));
    }

    @PutMapping("/{id}/confirmar-sinal") // Endpoint que aceita requisições PUT (atualizar) com parâmetro na URL
    @Operation(summary = "Funcionário confirma recebimento do sinal (AGUARDANDO_CONFIRMACAO → CONFIRMADO)") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<AgendamentoResponseDTO> confirmarSinal(@PathVariable Long id) { // Recebe o valor da URL (ex: /agendamentos/{id}/confirmar-sinal)
        return ResponseEntity.ok(agendamentoMapper.toResponse(service.confirmarSinal(id)));
    }

    @PutMapping("/{id}/registrar-chegada") // Endpoint que aceita requisições PUT (atualizar) com parâmetro na URL
    @Operation(summary = "Registrar chegada do cliente (CONFIRMADO → EM_ATENDIMENTO)") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<Map<String, Object>> registrarChegada(@PathVariable Long id) { // Recebe o valor da URL (ex: /agendamentos/{id}/registrar-chegada)
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

    @PutMapping("/{id}/finalizar") // Endpoint que aceita requisições PUT (atualizar) com parâmetro na URL
    @Operation(summary = "Finalizar atendimento e pagamento (EM_ATENDIMENTO → CONCLUIDO)") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<AgendamentoResponseDTO> finalizar(@PathVariable Long id) { // Recebe o valor da URL (ex: /agendamentos/{id}/finalizar)
        return ResponseEntity.ok(agendamentoMapper.toResponse(service.finalizarAtendimento(id)));
    }

    @PutMapping("/{id}/cancelar") // Endpoint que aceita requisições PUT (atualizar) com parâmetro na URL
    @Operation(summary = "Cancelar agendamento") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<AgendamentoResponseDTO> cancelar(
            @PathVariable Long id, // Recebe o valor da URL (ex: /agendamentos/{id}/cancelar)
            @RequestBody(required = false) Map<String, String> body) { // Recebe os dados do corpo da requisição HTTP (opcional)
        String motivo = body != null ? body.get("motivo") : null;
        return ResponseEntity.ok(agendamentoMapper.toResponse(service.cancelar(id, motivo, null)));
    }
    
    @PutMapping("/{id}") // Endpoint que aceita requisições PUT (atualizar) com parâmetro na URL
    public ResponseEntity<AgendamentoResponseDTO> atualizar(@PathVariable Long id, @RequestBody AgendamentoRequestDTO dto) { // Recebe o valor da URL e os dados do corpo da requisição HTTP (JSON)
        Agendamento entity = service.findById(id);
        entity.setDataHoraInicio(dto.getDataHoraInicio());
        entity.setDataHoraFim(dto.getDataHoraFim());
        entity.setValorTotal(dto.getValorTotal());
        entity.setValorSinal(dto.getValorSinal());
        entity.setQuantidadeParcelas(dto.getQuantidadeParcelas());
        entity.setObservacoes(dto.getObservacoes());
        return ResponseEntity.ok(agendamentoMapper.toResponse(service.update(id, entity)));
    }
    
    @DeleteMapping("/{id}") // Endpoint que aceita requisições DELETE (excluir) com parâmetro na URL
    public ResponseEntity<Void> excluir(@PathVariable Long id) { // Recebe o valor da URL (ex: /agendamentos/{id})
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/hoje") // Endpoint que aceita requisições GET
    @Operation(summary = "Agendamentos de hoje") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<List<AgendamentoResponseDTO>> hoje() {
        List<AgendamentoResponseDTO> dtos = service.findByData(LocalDate.now()).stream()
            .map(agendamentoMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/periodo") // Endpoint que aceita requisições GET
    public ResponseEntity<List<AgendamentoResponseDTO>> periodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio, // Recebe o valor do parâmetro da URL com formato de data ISO
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) { // Recebe o valor do parâmetro da URL com formato de data ISO
        List<AgendamentoResponseDTO> dtos = service.findByPeriodo(inicio, fim).stream()
            .map(agendamentoMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/status") // Endpoint que aceita requisições GET
    @Operation(summary = "Agendamentos por status") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<List<AgendamentoResponseDTO>> porStatus(@RequestParam StatusAgendamento status) { // Recebe o valor do parâmetro da URL (?param=valor)
        List<AgendamentoResponseDTO> dtos = service.findByStatus(status).stream()
            .map(agendamentoMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/funcionario/{id}") // Endpoint que aceita requisições GET com parâmetro na URL
    @Operation(summary = "Agendamentos por funcionário") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<List<AgendamentoResponseDTO>> porFuncionario(@PathVariable Long id) { // Recebe o valor da URL (ex: /agendamentos/funcionario/{id})
        List<AgendamentoResponseDTO> dtos = service.findByFuncionarioId(id).stream()
            .map(agendamentoMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/cliente/{id}") // Endpoint que aceita requisições GET com parâmetro na URL
    @Operation(summary = "Agendamentos por cliente") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<List<AgendamentoResponseDTO>> porCliente(@PathVariable Long id) { // Recebe o valor da URL (ex: /agendamentos/cliente/{id})
        List<AgendamentoResponseDTO> dtos = service.findByClienteId(id).stream()
            .map(agendamentoMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/proximos") // Endpoint que aceita requisições GET
    @Operation(summary = "Próximos agendamentos") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<List<AgendamentoResponseDTO>> proximos(@RequestParam(defaultValue = "7") int dias) { // Recebe o valor do parâmetro da URL com valor padrão
        LocalDate hoje = LocalDate.now();
        List<AgendamentoResponseDTO> dtos = service.findByPeriodo(hoje, hoje.plusDays(dias)).stream()
            .map(agendamentoMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/hoje/resumo") // Endpoint que aceita requisições GET
    @Operation(summary = "Resumo do dia") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<List<AgendamentoResponseDTO>> resumoDia() {
        List<AgendamentoResponseDTO> dtos = service.findByData(LocalDate.now()).stream()
            .map(agendamentoMapper::toResponse)
            .toList();
        return ResponseEntity.ok(dtos);
    }
    
    @PutMapping("/{id}/nao-compareceu") // Endpoint que aceita requisições PUT (atualizar) com parâmetro na URL
    @Operation(summary = "Não compareceu (CONFIRMADO/EM_ATENDIMENTO → NAO_COMPARECEU)") // Swagger: descreve o endpoint na documentação
    public ResponseEntity<AgendamentoResponseDTO> naoCompareceu(@PathVariable Long id) { // Recebe o valor da URL (ex: /agendamentos/{id}/nao-compareceu)
        return ResponseEntity.ok(agendamentoMapper.toResponse(service.naoCompareceu(id)));
    }

    @PutMapping("/{id}/reagendar")
    @Operation(summary = "Reagendar agendamento")
    public ResponseEntity<AgendamentoResponseDTO> reagendar(
            @PathVariable Long id, @RequestBody ReagendamentoRequestDTO dto) {
        return ResponseEntity.ok(agendamentoMapper.toResponse(premiumService.reagendar(id, dto)));
    }

    @GetMapping("/{id}/taxa-cancelamento")
    @Operation(summary = "Calcular taxa de cancelamento")
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
    @Operation(summary = "Cancelar com política de taxa")
    public ResponseEntity<AgendamentoResponseDTO> cancelarPremium(
            @PathVariable Long id, @RequestBody CancelamentoPremiumRequestDTO dto) {
        return ResponseEntity.ok(agendamentoMapper.toResponse(premiumService.cancelarComTaxa(id, dto)));
    }

    @GetMapping("/no-shows/{clienteId}")
    @Operation(summary = "Histórico de no-shows do cliente")
    public ResponseEntity<HistoricoNoShowDTO> historicoNoShows(@PathVariable Long clienteId) {
        return ResponseEntity.ok(premiumService.historicoNoShows(clienteId));
    }

    @GetMapping("/no-shows/{clienteId}/bloqueado")
    @Operation(summary = "Verifica se cliente está bloqueado por no-shows")
    public ResponseEntity<Map<String, Boolean>> clienteBloqueado(@PathVariable Long clienteId) {
        return ResponseEntity.ok(Map.of("bloqueado", premiumService.clienteBloqueadoPorNoShows(clienteId)));
    }

    @GetMapping("/sugestoes")
    @Operation(summary = "Sugerir horários alternativos próximos")
    public ResponseEntity<List<LocalTime>> sugestoes(
            @RequestParam Long servicoId,
            @RequestParam Long funcionarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataDesejada,
            @RequestParam(defaultValue = "7") int diasRange) {
        return ResponseEntity.ok(premiumService.sugerirHorariosProximos(servicoId, funcionarioId, dataDesejada, diasRange));
    }

    @PostMapping("/recorrente")
    @Operation(summary = "Criar agendamentos recorrentes")
    public ResponseEntity<List<AgendamentoResponseDTO>> criarRecorrente(@RequestBody AgendamentoRecorrenteRequestDTO dto) {
        List<AgendamentoResponseDTO> dtos = premiumService.criarRecorrente(dto).stream()
            .map(agendamentoMapper::toResponse).toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(dtos);
    }
}
