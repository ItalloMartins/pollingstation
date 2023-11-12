# Guia de Utilização da API

Para usar esta API, certifique-se de ter o Docker instalado. Após a instalação, execute o seguinte comando na pasta Docker da API:

```bash
docker-compose -d up
```
Para rodar em ambiente dev
```shell script
./gradlew quarkusDev
```
Para rodar normalmente pela IDE
```shell script
./gradlew quarkusRun
```

Em seguida, utilize a collection do Postman localizada na raiz da API, seguindo a lógica de uso descrita abaixo:

1. Criação de Pauta  
Certifique-se de que a pauta não esteja vazia.

3. Criação de Sessão  
Uma sessão necessita de uma pauta.
A sessão possui uma duração, contada a partir do momento da criação, somando o valor do campo "duracao".
Não há um indicativo explícito se a sessão está fechada; durante a votação, é validado se está ativa.
A cada 45 segundos, um método do framework realiza o envio para o Kafka*.

5. Voto  
Cada CPF pode votar apenas uma vez em cada sessão.
Não é necessário cadastrar o CPF; basta enviá-lo no método POST de votação, onde a persistência ou atualização é realizada, adicionando o voto.
Optei por armazenar os votos no CPF, utilizando um array para persistência. Isso é especialmente eficiente em bancos NoSQL, considerando que um CPF fará votos finitos. Mesmo em casos de um CPF muito ativo, levaria bastante tempo para atingir o limite de um documento. Caso necessário, estratégias podem ser implementadas para lidar com o crescimento do documento.
O CPF é validado pela API do Invertexto. Foi criado um token no application.yml para facilitar os testes sem a necessidade de criar uma conta.

Observações:

Para validar as mensagens no Kafka, o Kafdrop foi implementado no container. Para visualizar as mensagens, acesse http://localhost:9000/topic/votacao-encerrada/messages e clique em "View Messages". Não foram adicionados headers ou keys, pois não há necessidade.
