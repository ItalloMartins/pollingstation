package br.com.assemb.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class HabilitadoVotar {
    private boolean valid;
    private String formatted;

    public HabilitadoVotar(HabilitadoVotar habilitadoVotar) {
    }
}

