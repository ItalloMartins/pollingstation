package br.com.assemb.service;

import br.com.assemb.dto.PautaDTO;
import br.com.assemb.mapper.PautaMapper;
import br.com.assemb.model.PautaModel;
import br.com.assemb.repository.PautaRepository;
import br.com.assemb.service.interfaces.IntegracaoPautas;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.NoContentException;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

import java.util.List;

@Slf4j
@ApplicationScoped
public class PautaService implements IntegracaoPautas {

    @Inject
    PautaRepository pautaRepository;

    /**
     * Busca pauta por ID
     * @param objectId
     * @return
     */
    public PautaDTO getById(ObjectId objectId){
        log.info("PautaService.getByI: {}", objectId);
        PautaModel pautaModel = null;

        if (pautaModel == null){
            return null;
        }
        return PautaMapper.toDTO(pautaModel);
    }

    /**
     * Busca todos registros
     * @return
     * @throws NoContentException
     */
    public List<PautaDTO> getAll() throws NoContentException {
        log.info("PautaService.getAll ");

        List<PautaDTO> pautaDTOS = PautaMapper.toDTOList(
                this.pautaRepository.findAll(Sort.ascending("titulo").ascending())
        );
        if (pautaDTOS.isEmpty()){
            throw new NoContentException("Não existem pautas cadastradas.");
        }
        return pautaDTOS;
    }


    /**
     * Busca por titulo. Por enquanto apenas com o titulo correto.
     * @param titulo
     * @return
     */
    public PautaDTO getByTitulo(String titulo){
        log.info("PautaService.getByTitulo: {}", titulo);
        PautaModel pautaModel = this.pautaRepository.find("titulo", titulo).firstResult();
        if (pautaModel == null){
            throw new BadRequestException("Pauta não encontrada");
        }
        return PautaMapper.toDTO(pautaModel);
    }

    /**
     * Cria ou edita uma Pauta
     * @param pautaModel
     */
    public void createUpdate(PautaModel pautaModel){
        log.info("PautaService.createUpdate: {}", pautaModel);
        this.pautaRepository.persistOrUpdate(pautaModel);
    }

    /**
     * Deleta pauta
     * @param objectId
     */
    public void delete(ObjectId objectId){
        log.info("PautaService.delete ", objectId);
        this.pautaRepository.deleteById(objectId);
    }

    /**
     * Busca pauta por ID, utilizado por interface
     * @param id
     * @return
     */
    @Override
    public PautaDTO getPautaById(ObjectId id) {
        log.info("PautaService.getById: {}", id);
        PautaModel pautaModel = this.pautaRepository.findById(id);
        if (pautaModel == null){
            return null;
        }
        return PautaMapper.toDTO(pautaModel);
    }
}
