package br.com.assemb.controller;

import br.com.assemb.dto.votos.VotosPautaDTO;
import br.com.assemb.service.VotosService;
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
 * VotosRest tinha como objetivo registrar apenas os votos usando CPF, Id da Pauta e Id da sessão, porém usando como base
 * o CPF, como se fosse um cadastro no sistema, depois retirei o cadastro pois não fazia sentido.
 */
@Slf4j
@Path("/v1/votos")
public class VotosRest {

    @Inject
    VotosService votosService;

    /**
     * Cria o CPF ao votar numa sessão, ou edita os votos que o CPF já fez.
     * Como usei NoSQL, preferi criar um document que contém os votos do CPF, para assim tbm validar se ele já votou numa sessão.
     * @param cpf
     * @param sessao
     * @param pautaDTO
     * @return
     */
    @POST
    @Path("/{cpf}/{sessao}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerVoto(
            @PathParam("cpf") String cpf,
            @PathParam("sessao") ObjectId sessao,
            @Valid VotosPautaDTO pautaDTO) throws IOException {
        log.info("VotosRest.registerVoto: {}", cpf);
        this.votosService.validacoesVoto(cpf, sessao, pautaDTO);
        return Response.created(UriBuilder.fromResource(VotosRest.class).build()).build();
    }

    /**
     * Busca cpfs, ao usar roles configurar para user normal não poder utilizar a rota
     * @param cpf
     * @return
     * @throws NoContentException
     */
    @GET
    @Path("/{cpf}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCPF(
            @PathParam("cpf") String cpf) throws NoContentException {
        log.info("VotosRest.getAllCPF: {}");
        return Response.ok(this.votosService.getAll(cpf)).build();
    }

    @DELETE
    @Path("/{cpf}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("cpf") String cpf){
        log.info("SessaoRest.delete: {}", cpf);
        this.votosService.deleteCpf(cpf);
        return Response.created(UriBuilder.fromResource(PautaRest.class).build()).build();
    }
}
