package br.com.assemb.mapper;

import br.com.assemb.dto.SessaoDTO;
import br.com.assemb.model.SessaoModel;
import io.quarkus.mongodb.panache.PanacheQuery;
import lombok.extern.slf4j.Slf4j;
import java.util.List;


/**
 * Utilizo esses mappers, para fazer a troca de dto para entidade, com a ideia de que nem a camada rest, pode-se expor a
 * modelagem do banco, usando mappers é possível evitar que isso ocorra
 */
@Slf4j
public class SessaoMapper {
    public static SessaoDTO toDTO(SessaoModel sessaoModel){
        log.info("SessaoMapper.toDTO: {}", sessaoModel);

        SessaoDTO sessaoDTO = new SessaoDTO();
        sessaoDTO.setId(sessaoModel.getId());
        sessaoDTO.setTitulo(sessaoDTO.getTitulo());
        sessaoDTO.setPautas(sessaoModel.getPautas());
        sessaoDTO.setDuracao(sessaoModel.getDuracao());
        sessaoDTO.setAbertura(sessaoModel.getAbertura());
        sessaoDTO.setVotosSim(sessaoDTO.getVotosSim());
        sessaoDTO.setVotosNao(sessaoDTO.getVotosNao());
        return sessaoDTO;
    }

    public static SessaoModel toEntity(SessaoDTO sessaoDTO){
        log.info("SessaoMapper.toDTO: {}", sessaoDTO);

        SessaoModel sessaoModel = new SessaoModel();
        sessaoModel.setId(sessaoDTO.getId());
        sessaoModel.setTitulo(sessaoDTO.getTitulo());
        sessaoModel.setPautas(sessaoDTO.getPautas());
        sessaoModel.setDuracao((sessaoDTO.getDuracao() == null) ? 1 : sessaoDTO.getDuracao());
        sessaoModel.setAbertura(sessaoDTO.getAbertura());
        sessaoModel.setEncerramento(sessaoDTO.getEncerramento());
        sessaoModel.setVotoSim(sessaoDTO.getVotosSim());
        sessaoModel.setVotoNao(sessaoDTO.getVotosNao());
        return sessaoModel;
    }

    public static List<SessaoDTO> toDTOList(PanacheQuery<SessaoModel> sessaoModel){
        log.info("SessaoMapper.toDTOList: {}", sessaoModel);

        return sessaoModel.stream().map(
                result ->SessaoDTO.builder()
                        .id(result.getId())
                        .titulo(result.getTitulo())
                        .abertura(result.getAbertura())
                        .duracao(result.getDuracao())
                        .encerramento(result.getEncerramento())
                        .pautas(result.getPautas())
                        .votosSim(result.getVotoSim())
                        .votosNao(result.getVotoNao())
                        .build()
        ).toList();
    }
}
