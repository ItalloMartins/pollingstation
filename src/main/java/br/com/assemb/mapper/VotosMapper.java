package br.com.assemb.mapper;

import br.com.assemb.dto.votos.VotosDTO;
import br.com.assemb.dto.votos.VotosPautaDTO;
import br.com.assemb.dto.votos.VotosSessaoDTO;
import br.com.assemb.model.votos.VotosModel;
import br.com.assemb.model.votos.VotosPautaModel;
import br.com.assemb.model.votos.VotosSessaoModel;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Essa é única mapper diferente das outras, pois eu optei por usar o mapeamento composto, para poder aninhar
 * os resultados, e utilizar com mais facilidade, porém durante o desenvolvimento, acabei retirando a funcionalidade
 * de criar N pautas na sessão.
 */
@Slf4j
public class VotosMapper {

    public static VotosDTO toDTO(VotosModel model) {
        return VotosDTO.builder()
                .id(model.getId())
                .cpf(model.getCpf())
                .sessoesVotos(toSessaoDTOList(model.getSessoesVotos()))
                .build();
    }

    public static List<VotosDTO> toDTOList(List<VotosModel> models) {
        return models.stream()
                .map(VotosMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static VotosModel toModel(VotosDTO dto) {
        return VotosModel.builder()
                .id(dto.getId())
                .cpf(dto.getCpf())
                .sessoesVotos(toSessaoModelList(dto.getSessoesVotos()))
                .build();
    }

    private static VotosSessaoDTO toSessaoDTO(VotosSessaoModel sessaoModel) {
        return VotosSessaoDTO.builder()
                .sessaoId(sessaoModel.getSessaoId())
                .pautaId(toPautaDTO(sessaoModel.getPautaId()))
                .build();
    }

    private static VotosSessaoModel toSessaoModel(VotosSessaoDTO sessaoDTO) {
        return VotosSessaoModel.builder()
                .sessaoId(sessaoDTO.getSessaoId())
                .pautaId(toPautaModel(sessaoDTO.getPautaId()))
                .build();
    }

    private static VotosPautaDTO toPautaDTO(VotosPautaModel pautaModel) {
        return VotosPautaDTO.builder()
                .pautaId(pautaModel.getPautaId())
                .voto(pautaModel.isVoto())
                .build();
    }

    private static VotosPautaModel toPautaModel(VotosPautaDTO pautaDTO) {
        return VotosPautaModel.builder()
                .pautaId(pautaDTO.getPautaId())
                .voto(pautaDTO.isVoto())
                .build();
    }

    private static List<VotosSessaoDTO> toSessaoDTOList(List<VotosSessaoModel> sessaoModels) {
        return sessaoModels.stream()
                .map(VotosMapper::toSessaoDTO)
                .collect(Collectors.toList());
    }

    private static List<VotosSessaoModel> toSessaoModelList(List<VotosSessaoDTO> sessaoDTOs) {
        return sessaoDTOs.stream()
                .map(VotosMapper::toSessaoModel)
                .collect(Collectors.toList());
    }
}
