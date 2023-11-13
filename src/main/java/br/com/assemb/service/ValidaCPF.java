package br.com.assemb.service;


import br.com.assemb.dto.HabilitadoVotar;
import br.com.assemb.service.interfaces.ValidacaoCPF;
import br.com.assemb.service.out.SimplePostRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class ValidaCPF implements ValidacaoCPF {

    @Inject
    SimplePostRequest simplePostRequest;

    @Override
    public HabilitadoVotar validaAsync(String cpf) throws IOException {
        return simplePostRequest.enviaRequest(cpf);
    }
}
