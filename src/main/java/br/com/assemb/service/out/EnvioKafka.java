package br.com.assemb.service.out;

import br.com.assemb.dto.SessaoDTO;
import br.com.assemb.service.interfaces.IntegracaoSessao;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.NoContentException;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class EnvioKafka {
    @Inject
    IntegracaoSessao integracaoSessao;

    /**
     * Para enviar para kafka as mensagens, usei o @Outgoing que produz as mensagens, o problema é que fora da camada Rest
     * Sem o "gatilho" de um endpoint e sem o Emmiter, não é possível controlar muito bem o envio das msgs, o framework
     * apenas sai enviando até mesmo null, porém, ocasionando NPE, então uso o onOverflow().drop() para controlar o fluxo
     * e não permitir o envio
     * @return
     */
    @Outgoing("votacao-encerrada")
    public Multi<List<SessaoDTO>> sendSessaoUpdates() {
        return (Multi<List<SessaoDTO>>) Multi.createFrom().ticks().every(Duration.ofSeconds(45))
                .onOverflow().drop()
                .map(tick -> {
                    try {
                        List<SessaoDTO> updatedSessaoDTOS = integracaoSessao.buscaSessoesEncerradas();
                        if (updatedSessaoDTOS != null && !updatedSessaoDTOS.isEmpty()) {
                            return updatedSessaoDTOS;
                        }
                    } catch (NoContentException e) {
                        // Deveria ter um log descente, mas não faz sentido ter
                    }
                    return Collections.emptyList(); // Evita NPE, retornando uma lista vazia
                })
                .filter(list -> !list.isEmpty())
                .onItem().transformToMultiAndConcatenate(item -> Multi.createFrom().iterable(item)); // Transforma em Multi para emitir um item por vez
    }
}
