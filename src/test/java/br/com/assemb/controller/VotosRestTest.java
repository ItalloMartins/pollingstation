package br.com.assemb.controller;

import br.com.assemb.dto.votos.VotosPautaDTO;
import br.com.assemb.service.VotosService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.NoContentException;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.bson.types.ObjectId;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VotosRestTest {

    @Mock
    private VotosService votosService;

    @InjectMocks
    private VotosRest votosRest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterVoto() throws IOException {
        // Simula os parâmetros da requisição
        String cpf = "12345678901";
        ObjectId sessao = new ObjectId();
        VotosPautaDTO pautaDTO = new VotosPautaDTO();

        // Quando o método validacoesVoto for chamado, não faz nada
        doNothing().when(votosService).validacoesVoto(cpf, sessao, pautaDTO);

        // Chama o método do controlador
        Response response = votosRest.registerVoto(cpf, sessao, pautaDTO);

        // Verifica se o status da resposta é CREATED
        assertEquals(Status.CREATED.getStatusCode(), response.getStatus());

        // Verifica se o serviço foi chamado
        verify(votosService).validacoesVoto(cpf, sessao, pautaDTO);
    }

    @Test
    void testGetAllCPF() throws NoContentException {
        // Simula um CPF
        String cpf = "12345678901";

        // Quando o método getAll for chamado, retorna uma lista vazia
        when(votosService.getAll(cpf)).thenReturn(null);

        // Chama o método do controlador
        Response response = votosRest.getAllCPF(cpf);

        // Verifica se o status da resposta é OK
        assertEquals(Status.OK.getStatusCode(), response.getStatus());

        // Verifica se o serviço foi chamado
        verify(votosService).getAll(cpf);
    }

    @Test
    void testDelete() {
        // Simula um CPF
        String cpf = "12345678901";

        // Quando o método deleteCpf for chamado, não faz nada
        doNothing().when(votosService).deleteCpf(cpf);

        // Chama o método do controlador
        Response response = votosRest.delete(cpf);

        // Verifica se o status da resposta é CREATED
        assertEquals(Status.CREATED.getStatusCode(), response.getStatus());

        // Verifica se o serviço foi chamado
        verify(votosService).deleteCpf(cpf);
    }
}
