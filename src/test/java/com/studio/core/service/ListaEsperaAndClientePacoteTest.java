package com.studio.core.service;

import com.studio.core.dominio.cliente_pacote.entity.ClientePacote;
import com.studio.core.dominio.cliente_pacote.repository.ClientePacoteRepository;
import com.studio.core.dominio.lista_espera.entity.ListaEspera;
import com.studio.core.dominio.lista_espera.repository.ListaEsperaRepository;
import com.studio.core.exception.BadRequestException;
import com.studio.core.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListaEsperaServiceTest {

    @Mock private ListaEsperaRepository repository;
    @InjectMocks private ListaEsperaService service;

    @Test
    void marcarAtendido_DeveLancarErro_QuandoNaoAguardando() {
        ListaEspera item = new ListaEspera();
        item.setId(1L);
        item.setStatus(ListaEspera.StatusListaEspera.ATENDIDO);

        when(repository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(BadRequestException.class, () -> service.marcarAtendido(1L));
    }

    @Test
    void cancelar_DeveLancarErro_QuandoJaAtendido() {
        ListaEspera item = new ListaEspera();
        item.setId(1L);
        item.setStatus(ListaEspera.StatusListaEspera.ATENDIDO);

        when(repository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(BadRequestException.class, () -> service.cancelar(1L));
    }

    @Test
    void cancelar_DeveLancarErro_QuandoJaCancelado() {
        ListaEspera item = new ListaEspera();
        item.setId(1L);
        item.setStatus(ListaEspera.StatusListaEspera.CANCELADO);

        when(repository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(BadRequestException.class, () -> service.cancelar(1L));
    }
}

@ExtendWith(MockitoExtension.class)
class ClientePacoteServiceTest2 {

    @Mock private ClientePacoteRepository repository;
    @InjectMocks private ClientePacoteService service;

    @Test
    void usarSessao_DeveLancarErro_QuandoExpirado() {
        ClientePacote cp = new ClientePacote();
        cp.setId(1L);
        cp.setStatus(ClientePacote.StatusClientePacote.EXPIRADO);

        when(repository.findById(1L)).thenReturn(Optional.of(cp));

        assertThrows(BadRequestException.class, () -> service.usarSessao(1L));
    }

    @Test
    void usarSessao_DeveLancarErro_QuandoJaUsado() {
        ClientePacote cp = new ClientePacote();
        cp.setId(1L);
        cp.setStatus(ClientePacote.StatusClientePacote.USADO);

        when(repository.findById(1L)).thenReturn(Optional.of(cp));

        assertThrows(BadRequestException.class, () -> service.usarSessao(1L));
    }
}
