package br.com.assemb.service;

import br.com.assemb.dto.HabilitadoVotar;
import br.com.assemb.dto.PautaDTO;
import br.com.assemb.dto.SessaoDTO;
import br.com.assemb.dto.votos.VotosDTO;
import br.com.assemb.dto.votos.VotosPautaDTO;
import br.com.assemb.mapper.VotosMapper;
import br.com.assemb.model.votos.VotosModel;
import br.com.assemb.model.votos.VotosPautaModel;
import br.com.assemb.model.votos.VotosSessaoModel;
import br.com.assemb.repository.VotoRepository;
import br.com.assemb.service.interfaces.IntegracaoPautas;
import br.com.assemb.service.interfaces.IntegracaoSessao;
import br.com.assemb.service.interfaces.ValidacaoCPF;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.NoContentException;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

/**
 * Tem como objetivo gerar o registro do CPF, e orquestrar o voto em cada sessão
 */
@Slf4j
@ApplicationScoped
@RegisterForReflection
public class VotosService {

    private static final String CPF_NOT_FOUND = "CPF não encontrado";
    private static final String CPF_ALREADY_VOTED = "CPF já votou nessa sessão";
    private static final String NO_CPF_REGISTERED = "Não existem CPFs cadastrados.";

    @Inject
    VotoRepository votoRepository;

    @Inject
    IntegracaoPautas integracaoPautas;

    @Inject
    IntegracaoSessao integracaoSessao;

    @Inject
    ValidacaoCPF validacaoCPF;

    public void enviaVotacoes(){

    }

    /**
     *
     * @param cpf
     * @param sessao
     * @param pautaDto
     */
    public void validacoesVoto(String cpf, ObjectId sessao, VotosPautaDTO pautaDto) throws IOException {
        log.info("VotosService.registerVoto: {}", cpf);

        HabilitadoVotar buscaHabilitadoVotar = validacaoCPF.validaAsync(cpf);
        if (!buscaHabilitadoVotar.isValid()){
            throw new BadRequestException("CPF inválido.");
        }

        SessaoDTO sessaoDTOBusca = integracaoSessao.getSessaoById(sessao);
        if (sessaoDTOBusca == null) {
            throw new BadRequestException("Sessão não encontrada.");
        }

        PautaDTO pautaDTOBusca = integracaoPautas.getPautaById(pautaDto.getPautaId());
        if (pautaDTOBusca == null) {
            throw new BadRequestException("Pauta não encontrada.");
        }
        VotosModel buscaCPF = votoRepository.find("cpf", cpf).firstResult();
        if (buscaCPF == null) {
            criarVotoCPF(cpf, sessao, pautaDto);
        } else {
            registraVoto(buscaCPF, pautaDTOBusca, sessaoDTOBusca, cpf, sessao, pautaDto);
        }
    }

    /**
     * Registra o voto
     * @param buscaCPF
     * @param pautaDTOBusca
     * @param sessaoDTOBusca
     * @param cpf
     * @param sessao
     * @param pautaDto
     */
    private void registraVoto(
            VotosModel buscaCPF, PautaDTO pautaDTOBusca, SessaoDTO sessaoDTOBusca, String cpf, ObjectId sessao, VotosPautaDTO pautaDto) {
        Map<String, Object> params = new HashMap<>();
        params.put("cpf", cpf);
        params.put("pauta", pautaDTOBusca.getId());
        params.put("sessao", sessaoDTOBusca.getId());
        VotosModel buscaVotoNaSessaoCPF = votoRepository.find(
                        "cpf = :cpf and sessoesVotos.sessaoId = :sessao and sessoesVotos.pautaId.pautaId = :pauta", params)
                .firstResult();
        if (buscaVotoNaSessaoCPF != null) {
            throw new BadRequestException(CPF_ALREADY_VOTED);
        }

        VotosPautaModel votosPautaModel = new VotosPautaModel();
        votosPautaModel.setPautaId(pautaDto.getPautaId());
        votosPautaModel.setVoto(pautaDto.isVoto());

        VotosSessaoModel votosSessaoModel = new VotosSessaoModel();
        votosSessaoModel.setSessaoId(sessao);
        votosSessaoModel.setPautaId(votosPautaModel);

        List<VotosSessaoModel> adicionaVotos = new ArrayList<>(buscaCPF.getSessoesVotos());
        adicionaVotos.add(votosSessaoModel);

        buscaCPF.setSessoesVotos(adicionaVotos);

        adicionaVotos(sessaoDTOBusca.getId(), pautaDto.isVoto());
        votoRepository.update(buscaCPF);
    }

    /**
     * Registra CPF e seu primeiro voto
     * @param cpf
     * @param sessao
     * @param pautaDto
     */
    private void criarVotoCPF(String cpf, ObjectId sessao, VotosPautaDTO pautaDto) {
        VotosPautaModel votosPautaModel = new VotosPautaModel();
        votosPautaModel.setPautaId(pautaDto.getPautaId());
        votosPautaModel.setVoto(pautaDto.isVoto());

        VotosSessaoModel votosSessaoModel = new VotosSessaoModel();
        votosSessaoModel.setSessaoId(sessao);
        votosSessaoModel.setPautaId(votosPautaModel);

        List<VotosSessaoModel> adicionaVotos = new ArrayList<>();
        adicionaVotos.add(votosSessaoModel);

        VotosModel votosModel = new VotosModel();
        votosModel.setCpf(cpf);
        votosModel.setSessoesVotos(adicionaVotos);

        adicionaVotos(sessao, pautaDto.isVoto());

        votoRepository.persist(votosModel);
    }

    /**
     * Deleta CPF
     * @param cpf
     */
    public void deleteCpf(String cpf) {
        votoRepository.delete("cpf", cpf);
    }

    /**
     * Busca todos registros
     * @param cpf
     * @return
     * @throws NoContentException
     */
    public List<VotosDTO> getAll(String cpf) throws NoContentException {
        log.info("VotosService.getAll");

        List<VotosDTO> votosDTOS = VotosMapper.toDTOList(
                votoRepository.find("cpf", cpf).list()
        );

        if (votosDTOS.isEmpty()) {
            throw new NoContentException(NO_CPF_REGISTERED);
        }
        return votosDTOS;
    }

    /**
     * Adiciona um voto numa sessão
     * @param objectId
     * @param voto
     */
    public void adicionaVotos(ObjectId objectId, boolean voto){
        integracaoSessao.adicionaVoto(objectId, voto);
    }

}
