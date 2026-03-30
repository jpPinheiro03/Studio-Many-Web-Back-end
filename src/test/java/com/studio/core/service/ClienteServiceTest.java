package com.studio.core.service;

import com.studio.core.dominio.agendamento.repository.AgendamentoRepository;
import com.studio.core.dominio.cliente.entity.Cliente;
import com.studio.core.dominio.cliente.repository.ClienteRepository;
import com.studio.core.dominio.cliente_pacote.repository.ClientePacoteRepository;
import com.studio.core.dominio.lista_espera.repository.ListaEsperaRepository;
import com.studio.core.dominio.pedido.repository.PedidoRepository;
import com.studio.core.exception.BadRequestException;
import com.studio.core.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock private ClienteRepository repository;
    @Mock private AgendamentoRepository agendamentoRepository;
    @Mock private PedidoRepository pedidoRepository;
    @Mock private ClientePacoteRepository clientePacoteRepository;
    @Mock private ListaEsperaRepository listaEsperaRepository;

    @InjectMocks
    private ClienteService service;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setEmail("joao@teste.com");
        cliente.setTelefone("11999999999");
        cliente.setCpf("52998224725");
        cliente.setAtivo(true);
    }

    @Test
    void create_DeveCriarCliente() {
        when(repository.existsByEmail("joao@teste.com")).thenReturn(false);
        when(repository.save(any())).thenReturn(cliente);

        Cliente result = service.create(cliente);

        assertNotNull(result);
        assertEquals("João Silva", result.getNome());
    }

    @Test
    void create_DeveLancarErro_EmailDuplicado() {
        when(repository.existsByEmail("joao@teste.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> service.create(cliente));
    }

    @Test
    void create_DeveLancarErro_CpfInvalido() {
        cliente.setCpf("12345678901");
        when(repository.existsByEmail("joao@teste.com")).thenReturn(false);

        assertThrows(BadRequestException.class, () -> service.create(cliente));
    }

    @Test
    void delete_DeveExcluir_QuandoSemDependencias() {
        when(repository.existsById(1L)).thenReturn(true);
        when(agendamentoRepository.existsByCliente_Id(1L)).thenReturn(false);
        when(pedidoRepository.existsByCliente_Id(1L)).thenReturn(false);
        when(clientePacoteRepository.existsByCliente_Id(1L)).thenReturn(false);
        when(listaEsperaRepository.existsByCliente_Id(1L)).thenReturn(false);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void delete_DeveLancarErro_QuandoTemAgendamentos() {
        when(repository.existsById(1L)).thenReturn(true);
        when(agendamentoRepository.existsByCliente_Id(1L)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> service.delete(1L));
    }

    @Test
    void delete_DeveLancarErro_QuandoNaoExiste() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.delete(1L));
    }
}
