package br.com.assemb.dto;

import br.com.assemb.model.PautaModel;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class SessaoDTO {
    private ObjectId id;

    @NotBlank(message = "Título da sessão é obrigatório")
    private String titulo;

    @NotBlank(message = "Necessário uma pauta para cadastrar")
    private ObjectId pautas;

    private Integer duracao;

    private String abertura;

    private String encerramento;

    private Integer votosSim;

    private Integer votosNao;

}
