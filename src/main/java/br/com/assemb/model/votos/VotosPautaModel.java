package br.com.assemb.model.votos;

import lombok.*;
import org.bson.types.ObjectId;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class VotosPautaModel {
    private ObjectId pautaId;
    private boolean voto;
}
