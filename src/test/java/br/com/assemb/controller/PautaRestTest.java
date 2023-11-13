package br.com.assemb.controller;

import br.com.assemb.dto.PautaDTO;
import br.com.assemb.model.PautaModel;
import br.com.assemb.service.PautaService;
import br.com.assemb.utils.exceptions.ExceptionHandler;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import jakarta.ws.rs.core.NoContentException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class PautaRestTest {

    @Mock
    private PautaService pautaService;
    @InjectMocks
    private PautaRest pautaRest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPautas() throws ExceptionHandler, NoContentException {
        PautaDTO pautaDTOS = new PautaDTO();
        when(pautaService.getByTitulo("Titulo")).thenReturn(pautaDTOS);
        Response response = pautaRest.getPautas("Titulo");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void testGetAllPautas() throws NoContentException {
        List<PautaDTO> listaPautas = Arrays.asList(
                new PautaDTO(new ObjectId(), "Titulo", "Descrição", 10),
                new PautaDTO(new ObjectId(), "Titulo segundo", "Descrição segunda", 20)
        );
        when(pautaService.getAll()).thenReturn(listaPautas);

        Response response = pautaRest.getAllPautas();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(pautaService).getAll();
    }

    @Test
    void testCreate() {
        PautaModel pautaModel = new PautaModel(new ObjectId(), "Titulo", "Descrição");
        doNothing().when(pautaService).createUpdate(pautaModel);

        Response response = pautaRest.create(pautaModel);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        verify(pautaService).createUpdate(pautaModel);
    }

    @Test
    void testUpdate() {
        PautaModel pautaModel = new PautaModel(new ObjectId(), "Titulo", "Descrição");
        doNothing().when(pautaService).createUpdate(pautaModel);

        Response response = pautaRest.update(pautaModel);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        verify(pautaService).createUpdate(pautaModel);
    }

    @Test
    void testDelete() {
        ObjectId objectId = new ObjectId(String.valueOf(new ObjectId()));
        doNothing().when(pautaService).delete(objectId);

        Response response = pautaRest.delete(objectId);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        verify(pautaService).delete(objectId);
    }
}
