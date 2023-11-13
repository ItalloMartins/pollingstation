package br.com.assemb.model.votos;

import br.com.assemb.dto.votos.VotosSessaoDTO;
import lombok.*;
import org.bson.types.ObjectId;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class VotosSessaoModel {
    private ObjectId sessaoId;
    private VotosPautaModel pautaId;

}
