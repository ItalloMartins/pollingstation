package br.com.assemb.utils.exceptions;

import br.com.assemb.dto.JsonResponseDTO;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAcceptableException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.NoContentException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * Classe criada para lidar com exceptions, e retornar num padrão já definido na JsonResponseDTO.
 */
@Slf4j
@Provider
public class ExceptionHandler extends Throwable implements ExceptionMapper<Exception> {

  @Override
  public Response toResponse(Exception exception) {
    log.warn("ExceptionHandler.toResponse: {}", exception.getMessage(), exception);

    Response.Status statusCode = Response.Status.INTERNAL_SERVER_ERROR;
    List<String> message = Arrays.asList("Internal Server Error");
    /**
     * É possível implementar qualquer exception dessa forma.
     * Com um throw new Excetion é retornado a exception e a mensagem que é desiginada no tratamento da exception
     */
    if (exception instanceof BadRequestException) {
      statusCode = Response.Status.BAD_REQUEST;
      message = Arrays.asList(exception.getMessage());

      if (message.get(0).startsWith("[")) {
        message = Arrays.asList(message.get(0).replaceAll("[\\[\\]\"]", "").split("\\s*,\\s*"));
      }
    }
    if (exception instanceof NotAcceptableException) {
      statusCode = Response.Status.PRECONDITION_REQUIRED;
      message = Arrays.asList(exception.getMessage());

      if (message.get(0).startsWith("[")) {
        message = Arrays.asList(message.get(0).replaceAll("[\\[\\]\"]", "").split("\\s*,\\s*"));
      }
    }

    if (exception instanceof NoContentException) {
      statusCode = Response.Status.NO_CONTENT;
    }

    JsonResponseDTO jsonResponseDTO = new JsonResponseDTO();
    jsonResponseDTO.setError(message);

    return Response.status(statusCode).entity(jsonResponseDTO).build();
  }
}