package br.com.assemb.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@MongoEntity(collection = "sessao")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class SessaoModel {
    private ObjectId id;

    private String titulo;

    private ObjectId pautas;

    private Integer duracao;

    private String abertura;

    private String encerramento;

    private Integer votoSim;
    private Integer votoNao;
}
