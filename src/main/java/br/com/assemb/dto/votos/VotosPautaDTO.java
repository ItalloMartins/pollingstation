package br.com.assemb.dto.votos;

import lombok.*;
import org.bson.types.ObjectId;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class VotosPautaDTO {
    private ObjectId pautaId;
    private boolean voto;
}
