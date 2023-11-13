package br.com.assemb.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@MongoEntity(collection = "pauta")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class PautaModel {
    private ObjectId id;

    @NotBlank(message = "Título da pauta é obrigatório")
    private String titulo;
    private String descricao;
}
