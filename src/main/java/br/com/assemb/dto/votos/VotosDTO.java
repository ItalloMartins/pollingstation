package br.com.assemb.dto.votos;

import lombok.*;
import org.bson.types.ObjectId;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class VotosDTO {
    private ObjectId id;

    private String cpf;
    private List<VotosSessaoDTO> sessoesVotos;


}

