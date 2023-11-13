package br.com.assemb.service.interfaces;

import br.com.assemb.dto.HabilitadoVotar;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

public interface ValidacaoCPF {

    HabilitadoVotar validaAsync(String cpf) throws IOException;
}
