package br.com.assemb.service;

import br.com.assemb.dto.PautaDTO;
import br.com.assemb.dto.SessaoDTO;
import br.com.assemb.mapper.SessaoMapper;
import br.com.assemb.model.SessaoModel;
import br.com.assemb.repository.SessaoRepository;
import br.com.assemb.service.interfaces.IntegracaoPautas;
import br.com.assemb.service.interfaces.IntegracaoSessao;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.NoContentException;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A sessão tem como objetivo guardar os vatos a favor e contra da sessão, levando em consideração a data de encerramento.
 * A sessão não pode ser editade para evitar fraudes.
 */
@Slf4j
@ApplicationScoped
public class SessaoService implements IntegracaoSessao {

    @Inject
    SessaoRepository sessaoRepository;

    @Inject
    IntegracaoPautas buscas;

    /**
     * Busca todas sessões, é possível filtrar pelas datas
     * @param dataAbertura
     * @param dataEncerramento
     * @return
     * @throws NoContentException
     */
    public List<SessaoDTO> getAll(String dataAbertura, String dataEncerramento) throws NoContentException {
        log.info("SessaoService.getAll ");
        Map<String, Object> params = new HashMap<>();
        params.put("abertura", dataAbertura);
        params.put("encerramento", dataEncerramento);

        PanacheQuery<SessaoModel> sessaoBusca = null;
        if (dataAbertura != null && dataEncerramento != null) {
            sessaoBusca = this.sessaoRepository
                    .find("abertura like :abertura and encerramento like :encerramento", params);
        } else if (dataAbertura != null) {
            sessaoBusca = this.sessaoRepository
                    .find("abertura like :abertura", params);
        } else if (dataEncerramento != null) {
            sessaoBusca = this.sessaoRepository
                    .find("encerramento like :encerramento", params);
        } else {
            sessaoBusca = this.sessaoRepository.findAll(Sort.descending("abertura").ascending("encerramento"));
        }
        List<SessaoDTO> sessaoDTOS = SessaoMapper.toDTOList(sessaoBusca);

        if (sessaoDTOS.isEmpty()){
            throw new NoContentException("Não existem sessões cadastradas.");
        }
        return sessaoDTOS;
    }

    /**
     * Cria uma sessão, necessário uma pauta existente para isso
     * @param sessaoDTO
     */
    public void create(SessaoDTO sessaoDTO){
        log.info("SessaoService.create: {}", sessaoDTO);

        PautaDTO pautaDTO = buscas.getPautaById(sessaoDTO.getPautas());
        if (pautaDTO == null){
            throw new BadRequestException("Pauta com id " + sessaoDTO.getPautas() + " não existe");
        }

        LocalDateTime dataHoraAtual = LocalDateTime.now();
        int minutosParaAdicionar = (sessaoDTO.getDuracao() == null) ? 1 : sessaoDTO.getDuracao();

        LocalDateTime dataHoraFutura = dataHoraAtual.plusMinutes(minutosParaAdicionar);
        sessaoDTO.setAbertura(dataHoraAtual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        sessaoDTO.setEncerramento(dataHoraFutura.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        sessaoDTO.setVotosSim(0);
        sessaoDTO.setVotosNao(0);

        this.sessaoRepository.persist(SessaoMapper.toEntity(sessaoDTO));
    }

    /**
     * Deleta Sessão
     * @param objectId
     */
    public void delete(ObjectId objectId){
        log.info("SessaoService.delete ", objectId);
        this.sessaoRepository.deleteById(objectId);
    }

    /**
     * Valida se a votação está encerrada
     * @param dataAtual
     * @param dataEncerramento
     * @return
     */
    public boolean isVotacaoEncerrada(LocalDateTime dataAtual, String dataEncerramento){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime dataBancoLocalDateTime = LocalDateTime.parse(dataEncerramento, formatter);
        return dataAtual.isAfter(dataBancoLocalDateTime);
    }

    /**
     * Busca sessão por Id, usado por interface
     * @param objectId
     * @return
     */
    @Override
    public SessaoDTO getSessaoById(ObjectId objectId) {
        log.info("SessaoService.getPautaById: {}", objectId);
        SessaoModel sessaoModel = this.sessaoRepository.findById(objectId);
        if (sessaoModel == null){
            return null;
        }
        return SessaoMapper.toDTO(sessaoModel);
    }

    /**
     * Adiciona voto à sessão, segue uma série de regras para computar o voto
     * @param objectId
     * @param voto
     */
    @Override
    public void adicionaVoto(ObjectId objectId, boolean voto) {
        log.info("SessaoService.adicionaVoto {}", objectId);
        SessaoModel sessaoModel = this.sessaoRepository.findById(objectId);
        LocalDateTime dataHoraAtual = LocalDateTime.now();
        if (isVotacaoEncerrada(dataHoraAtual, sessaoModel.getEncerramento())){
            throw new BadRequestException("Votação está encerrada.");
        }
        if (voto){
            sessaoModel.setVotoSim(sessaoModel.getVotoSim()+1);
        } else {
            sessaoModel.setVotoNao(sessaoModel.getVotoNao()+1);
        }
        this.sessaoRepository.update(sessaoModel);
    }

    @Override
    public List<SessaoDTO> buscaSessoesEncerradas() {
        System.out.println("asdasdasdasdasdasdasd");
        LocalDateTime dataHoraAtual = LocalDateTime.now();
        Map<String, Object> params = new HashMap<>();
        params.put("encerramento", dataHoraAtual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        PanacheQuery<SessaoModel> sessaoBusca = this.sessaoRepository
                .find("encerramento like :encerramento", params);
        List<SessaoDTO> sessaoDTOS = SessaoMapper.toDTOList(sessaoBusca);

        if (!sessaoDTOS.isEmpty()){
            return sessaoDTOS;
        }
        return null;
    }
}
