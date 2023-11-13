package br.com.assemb.mapper;

import br.com.assemb.dto.PautaDTO;
import br.com.assemb.model.PautaModel;
import io.quarkus.mongodb.panache.PanacheQuery;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Utilizo esses mappers, para fazer a troca de dto para entidade, com a ideia de que nem a camada rest, pode-se expor a
 * modelagem do banco, usando mappers é possível evitar que isso ocorra
 */
@Slf4j
public class PautaMapper {
    public static PautaDTO toDTO(PautaModel pautaModel){
        log.info("PautaMapper.toDTO: {}", pautaModel);

        PautaDTO pautaDTO = new PautaDTO();
        pautaDTO.setId(pautaModel.getId());
        pautaDTO.setTitulo(pautaModel.getTitulo());
        pautaDTO.setDescricao(pautaModel.getDescricao());

        return pautaDTO;
    }

    public static List<PautaDTO> toDTOList(PanacheQuery<PautaModel> pautaModel){
        log.info("PautaMapper.toDTOList: {}", pautaModel);

        return pautaModel.stream().map(
                result ->PautaDTO.builder()
                        .id(result.getId())
                        .titulo(result.getTitulo())
                        .descricao(result.getDescricao())
                        .build()
        ).toList();
    }
}
