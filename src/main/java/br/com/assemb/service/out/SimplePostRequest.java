package br.com.assemb.service.out;

import br.com.assemb.dto.HabilitadoVotar;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotAcceptableException;
import jakarta.ws.rs.NotAuthorizedException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Tive que criar essa classe, pois no quarkus, ainda há algumas dificuldade em questão de deserialização, é possível chamar
 * endpoints, websockets com um @RegisterClient e uma interface, porém é necessário deserializar corretamente, e as vezes
 * demora demais e requer muitos testes, então para acelerar, criei um simples.
 */
@ApplicationScoped
public class SimplePostRequest {

    @ConfigProperty(name = "validator.token")
    String tokenCpf;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Para validar o cpf, preferi utilizar o serviço externo, do que criar uma validação na minha API.
     * Para utilizar, criei um token pessoal na minha conta para acelerar para os avaliadores, porém, se estiver vencido,
     * é só criar uma conta no invertexto, e adicioar o token gerado no yml validador.token
     * @param cpfValidar
     * @return
     * @throws IOException
     */
    public HabilitadoVotar enviaRequest(String cpfValidar) throws IOException {
        try {
            String apiUrl = "https://api.invertexto.com/v1/validator?token=" +
                    tokenCpf +
                    "&value=" +
                    cpfValidar;

            if ("".equals(tokenCpf)){
                throw new NotAcceptableException("Por favor, adicione o token do https://api.invertexto.com/tokens");
            }
            return doPostRequest(apiUrl);
        } catch (IOException e) {
            throw new IOException("Erro na chamada de validação de CPF.");
        }
    }

    public static HabilitadoVotar doPostRequest(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);

        // Lê a resposta
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return objectMapper.readValue(response.toString(), HabilitadoVotar.class);
        } finally {
            connection.disconnect();
        }
    }
}
