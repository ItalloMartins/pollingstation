package br.com.assemb.service.interfaces;

import br.com.assemb.dto.PautaDTO;
import org.bson.types.ObjectId;

/**
 * Utilizei as interfaces para controlar melhor onde o código é usado. Nesse caso, eu quis prender a implementação, para
 * que apenas pudesse ser feita a busca da Pauta no service correto.
 */
public interface IntegracaoPautas {

    /**
     * Busca a pauta pelo id
     * @param objectId
     * @return
     */
    PautaDTO getPautaById(ObjectId objectId);
}
