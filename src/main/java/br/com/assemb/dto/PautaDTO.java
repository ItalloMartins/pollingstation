package br.com.assemb.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.bson.types.ObjectId;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class PautaDTO {
    private ObjectId id;

    @NotBlank(message = "Necessário um titulo para cadastrar")
    private String titulo;
    @NotBlank(message = "Necessário uma descrição para cadastrar")
    private String descricao;
    private Integer totalVotos;
}
