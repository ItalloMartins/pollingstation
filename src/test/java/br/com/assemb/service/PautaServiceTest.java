package br.com.assemb.service;

import br.com.assemb.dto.PautaDTO;
import br.com.assemb.mapper.PautaMapper;
import br.com.assemb.model.PautaModel;
import br.com.assemb.repository.PautaRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.NoContentException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class PautaServiceTest {

    @Mock
    PautaRepository pautaRepository;

    @Mock
    PautaService pautaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetById() {
        ObjectId objectId = new ObjectId();

        PautaDTO pautaDTOS = mock(PautaDTO.class);
        PautaModel pautaModel = mock(PautaModel.class);

        when(pautaService.getById(objectId)).thenReturn(pautaDTOS);
        PautaDTO result = pautaService.getById(objectId);

        assertNotNull(result);
        assertEquals(pautaDTOS.getTitulo(), result.getTitulo());
        assertEquals(pautaDTOS.getDescricao(), result.getDescricao());
        assertEquals(pautaDTOS.getTotalVotos(), result.getTotalVotos());

        when(pautaRepository.findById(objectId)).thenReturn(pautaModel);
        pautaRepository.findById(objectId);
        verify(pautaRepository, times(1)).findById(objectId);
    }

    @Test
    void testGetAll() throws NoContentException {
        PautaDTO pautaDTO1 = mock(PautaDTO.class);
        PautaDTO pautaDTO2 = mock(PautaDTO.class);
        String titulo = "titulo";
        PanacheQuery<PautaModel> pautaModelPanacheQuery = mock(PanacheQuery.class);
        List<PautaModel> pautaModel = new ArrayList<>();
        List<PautaDTO> pautaDTOList = new ArrayList<>();
        pautaDTOList.add(pautaDTO1);
        pautaDTOList.add(pautaDTO2);

        when(pautaRepository.findAll(Sort.ascending("titulo").ascending())).
                thenReturn(pautaModelPanacheQuery);

        when(pautaService.getAll()).thenReturn(pautaDTOList);
        List<PautaDTO> result = pautaService.getAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(pautaDTOList.size(), result.size());
        assertEquals(pautaDTO1.getTitulo(), result.get(0).getTitulo());
        assertEquals(pautaDTO1.getDescricao(), result.get(0).getDescricao());
        assertEquals(pautaDTO1.getTotalVotos(), result.get(0).getTotalVotos());

        pautaRepository.findAll();
        verify(pautaRepository, times(1)).findAll();
    }

    @Test
    void testgetByTitulo() {

        PautaDTO pautaDTOS = mock(PautaDTO.class);

        String titulo = "";
        Object param = mock(Object.class);
        when(pautaService.getByTitulo(titulo)).thenReturn(pautaDTOS);

        PautaDTO result = pautaService.getByTitulo(titulo);

        assertNotNull(result);
        assertEquals(pautaDTOS.getTitulo(), result.getTitulo());
        assertEquals(pautaDTOS.getDescricao(), result.getDescricao());
        assertEquals(pautaDTOS.getTotalVotos(), result.getTotalVotos());

//        pautaRepository.find("titulo", titulo).firstResult();
        PautaModel model = mock(PautaModel.class);
        when(pautaRepository.find("titulo", titulo).firstResult()).thenReturn(model);
        verify(pautaRepository, times(1)).find("titulo", titulo).firstResult();
    }
}
