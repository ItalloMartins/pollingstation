package br.com.assemb.model.votos;

import br.com.assemb.dto.votos.VotosDTO;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.*;
import org.bson.types.ObjectId;

import java.util.List;

@MongoEntity(collection = "votos")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class VotosModel {
    private ObjectId id;

    private String cpf;
    private List<VotosSessaoModel> sessoesVotos;

}

