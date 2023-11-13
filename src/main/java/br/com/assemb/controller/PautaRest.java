package br.com.assemb.controller;

import br.com.assemb.dto.HabilitadoVotar;
import br.com.assemb.model.PautaModel;
import br.com.assemb.service.interfaces.ValidacaoCPF;
import br.com.assemb.service.out.SimplePostRequest;
import br.com.assemb.service.PautaService;
import br.com.assemb.utils.exceptions.ExceptionHandler;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NoContentException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

import java.io.IOException;

/**
 * PautaRest tem como objetivo manter as pautas. Criei com o objetivo de usar mais de uma pauta em cada sessão, para
 * se assemelhar com uma votação de verdade, em que numa sessão, vota-se em X pautas. mantive uma única pauta, pois
 * o enunciado pedia uma pauta por sessão.
 */
@Slf4j
@Path("/v1/pautas")
public class PautaRest {

    @Inject
    PautaService pautaService;

    /**
     * Busca por titulo
     * @param titulo
     * @return
     * @throws ExceptionHandler
     */
    @GET
    @Path("{titulo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPautas(@PathParam("titulo") String titulo) throws ExceptionHandler {
        log.info("PautaRest.getPautas: {}", titulo);
        return Response.ok(this.pautaService.getByTitulo(titulo)).build();
    }

    /**
     * Lista todas pautas
     * @return
     * @throws NoContentException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPautas() throws NoContentException {
        log.info("PautaRest.getAllPautas: {}");
        return Response.ok(this.pautaService.getAll()).build();
    }

    /**
     * Cria uma pauta
     * @param pautaModel
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid PautaModel pautaModel){
        log.info("PautaRest.create: {}", pautaModel);
        this.pautaService.createUpdate(pautaModel);
        return Response.created(UriBuilder.fromResource(PautaRest.class).build()).build();
    }

    /**
     * Edita uma pauta
     * @param pautaModel
     * @return
     */
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@Valid PautaModel pautaModel){
        log.info("PautaRest.update: {}", pautaModel);
        this.pautaService.createUpdate(pautaModel);
        return Response.created(UriBuilder.fromResource(PautaRest.class).build()).build();
    }

    /**
     * Deleta Pauta
     * @param objectId
     * @return
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") ObjectId objectId){
        log.info("PautaRest.delete: {}", objectId);
        this.pautaService.delete(objectId);
        return Response.created(UriBuilder.fromResource(PautaRest.class).build()).build();
    }
}
