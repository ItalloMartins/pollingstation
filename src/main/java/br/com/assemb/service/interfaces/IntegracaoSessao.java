package br.com.assemb.service.interfaces;

import br.com.assemb.dto.SessaoDTO;
import jakarta.ws.rs.core.NoContentException;
import org.bson.types.ObjectId;

import java.util.List;

public interface IntegracaoSessao {

    /**
     * Busca sessão por ID
     * @param objectId
     * @return
     */
    SessaoDTO getSessaoById(ObjectId objectId);

    /**
     * Adiciona voto na sessão
     * @param objectId
     * @param voto
     */
    void adicionaVoto(ObjectId objectId, boolean voto);

    List<SessaoDTO> buscaSessoesEncerradas() throws NoContentException;
}
