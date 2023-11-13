package br.com.assemb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Criei esse classe para ajudar a padronizar os retornos ao usuário.
 *
 */
@Getter
@Setter
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class JsonResponseDTO {

  private JsonResponseErrorDTO error;

  private Object data;

  public void setError(Object message) {
    this.error = new JsonResponseErrorDTO(message);
  }

  @Getter
  @Setter
  @AllArgsConstructor
  public class JsonResponseErrorDTO {

    private Object message;
  }
}