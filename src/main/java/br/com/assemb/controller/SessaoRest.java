package br.com.assemb.controller;

import br.com.assemb.dto.SessaoDTO;
import br.com.assemb.model.PautaModel;
import br.com.assemb.model.SessaoModel;
import br.com.assemb.service.SessaoService;
import br.com.assemb.service.interfaces.IntegracaoSessao;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NoContentException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

/**
 * A lógica que pensei de início, era uma abertura de sessão, requer X pautas, cada pauta seria votada individualmente
 * Mas entendi no enunciado que uma sessão tem apenas uma pauta, e o voto seria para aprovar ou não aquela cessão, não
 * levando em consideração as pautas individualmente
 */
@Slf4j
@Path("/v1/sessao")
public class SessaoRest {

    @Inject
    SessaoService sessaoService;

    @Inject
    IntegracaoSessao integracaoSessao;

    /**
     * Busca sessão, com possibilidade de buscar por data abertura e/ou encerramento
     * @param dataAbertura
     * @param dataEncerramento
     * @return
     * @throws NoContentException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSessao(
            @QueryParam("data_abertura") String dataAbertura,
            @QueryParam("data_encerramento") String dataEncerramento) throws NoContentException {
        log.info("SessaoRest.getAllSessao: {}");
        return Response.ok(this.sessaoService.getAll(dataAbertura, dataEncerramento)).build();
    }

    /**
     * Cria uma sessão, uma pauta existente precisa ser enviada.
     * @param sessaoDTO
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid SessaoDTO sessaoDTO){
        log.info("SessaoRest.create: {}", sessaoDTO);
        this.sessaoService.create(sessaoDTO);
        return Response.created(UriBuilder.fromResource(SessaoRest.class).build()).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") ObjectId objectId){
        log.info("SessaoRest.delete: {}", objectId);
        this.sessaoService.delete(objectId);
        return Response.created(UriBuilder.fromResource(PautaRest.class).build()).build();
    }
}
