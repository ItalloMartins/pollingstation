package br.com.assemb.controller;

import br.com.assemb.dto.SessaoDTO;
import br.com.assemb.model.PautaModel;
import br.com.assemb.service.SessaoService;
import br.com.assemb.service.interfaces.IntegracaoSessao;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.NoContentException;
import jakarta.ws.rs.core.Response.Status;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessaoRestTest {

    @Mock
    private SessaoService sessaoService;

    @Mock
    private IntegracaoSessao integracaoSessao;

    @InjectMocks
    private SessaoRest sessaoRest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSessao() throws NoContentException {
        // Simulando uma lista de sessões
        List<SessaoDTO> sessaoDTOS = Arrays.asList(
                new SessaoDTO(new ObjectId(), "Titulo", new ObjectId(),
                        100, "11/11/2023 12:15:10", "12/11/2023 12:15:10", 1, 2),
                new SessaoDTO(new ObjectId(), "Titulo", new ObjectId(),
                        100, "11/11/2023 12:15:10", "12/11/2023 12:15:10", 1, 2)
        );

        // Quando o método getAll for chamado, retorna a lista simulada
        when(sessaoService.getAll(null, null)).thenReturn(sessaoDTOS);

        // Chama o método do controlador
        Response response = sessaoRest.getAllSessao(null, null);

        // Verifica se o status da resposta é OK
        assertEquals(Status.OK.getStatusCode(), response.getStatus());

        // Verifica se o serviço foi chamado
        verify(sessaoService).getAll(null, null);
    }

    @Test
    void testCreate() {
        // Simula uma SessaoDTO
        SessaoDTO sessaoDTO = new SessaoDTO(new ObjectId(), "Titulo", new ObjectId(),
                100, "11/11/2023 12:15:10", "12/11/2023 12:15:10", 1, 2);

        // Quando o método create for chamado, não faz nada
        doNothing().when(sessaoService).create(sessaoDTO);

        // Chama o método do controlador
        Response response = sessaoRest.create(sessaoDTO);

        // Verifica se o status da resposta é CREATED
        assertEquals(Status.CREATED.getStatusCode(), response.getStatus());

        // Verifica se o serviço foi chamado
        verify(sessaoService).create(sessaoDTO);
    }

    @Test
    void testDelete() {
        // Simula um ObjectId
        ObjectId objectId = new ObjectId();

        // Quando o método delete for chamado, não faz nada
        doNothing().when(sessaoService).delete(objectId);

        // Chama o método do controlador
        Response response = sessaoRest.delete(objectId);

        // Verifica se o status da resposta é CREATED
        assertEquals(Status.CREATED.getStatusCode(), response.getStatus());

        // Verifica se o serviço foi chamado
        verify(sessaoService).delete(objectId);
    }
}
