# Testes API para a aplicação Web Lojinha

> Esse projeto traz de forma prática como analizar a qualidade de uma API de uma aplicação a partir de casos de testes manuais e automatizados.

## 🚀 Começando

Projeto de testes voltado para o App Lojinha desenvolvido no [Programa de Testes e Qualidade de Software Julio de Lima](https://mentoria.juliodelima.com.br/). 

A aplicação web possui APIs para:
1. CRUD de usuarios
2. CRUD de Produtos
3. CRUD de componentes de produtos
   
<img src="/images/swagger.png" height="300"> 

Funcionalidades gerais:
1. Adicionar usuarios
2. Adicionar novos produtos com ou sem componentes
3. Excluir produtos
4. Editar produtos
5. Sair da lojinha.

Vídeos do app funcionando clique [aqui](https://drive.google.com/file/d/1SjSIc3uqDEuE_9K5n3M3jkGoFqX3e8F1/view?usp=sharing)
   
## 🚧 Fluxo de trabalho

O projeto ainda está em desenvolvimento e as próximas atualizações serão voltadas nas seguintes tarefas:

1. - [x] Abordagens e conceitos usados;
2. - [ ] Testes Exploratorios usando o Swagger e Postman;
2. - [x] Criar o primeiro caso de teste baseado em uma regra de negócio para ser automatizado; 
3. - [x] Automatizar o caso de teste criado;
4. - [ ] Criar casos de testes que cubram as todas regras de negócio ;
5. - [ ] Automatizar todos os casos de testes;
6. - [ ] Tradução do projeto para o inglês;

## 🛠️ Construído com:

* [Intellij](https://www.jetbrains.com/pt-br/idea/) - Ferramenta para escrita do código dos testes automatizados
* [Maven](https://maven.apache.org/) - Gerente de Dependência
* [Selenium](https://www.selenium.dev/) - Ferramenta de automação de testes para aplicações web, permitindo a simulação de interações de usuários em navegadores.
* [JUnit](https://junit.org/junit4/) - Framework de testes unitários em Java, utilizada para garantir a qualidade e a funcionalidade do código por meio de testes automatizados.
* [Postman](https://www.postman.com) - Ferramenta para desenvolvimento, teste e documentação de APIs, facilitando a automação de requisições e a colaboração entre equipes.
* [Swagger](https://swagger.io) - Um inspetor de GUI para aplicativos móveis
* [Page Object Pattern](https://www.selenium.dev/documentation/test_practices/encouraged/page_object_models/) - Padrão utilizado para descrever e documentar o fluxo dos testes.
* [Gherkin Reference](https://cucumber.io/docs/gherkin/) - Padrão utilizado para escrita dos casos de testes.


## ⚙️ Executando os testes
### 📚 Abordagens e conceitos usados
  Interface de programação de aplicações), é um tipo de implementação de software que permite expor uma aplicação backend ao mundo exterior sem que haja acesso ao código fonte.
  entre os tipos mais utilizados de API temos a REST - representational state transfer (transferência de estado representacional) e a SOAP (Simple Object Access Protocol). Nesse estudo foi utilizado a API do tipo REST.

A arquitetura REST propõe que a API exponha os recursos de aplicação em um servidor e que esses recursos possam ser acessados através de métodos, deletar, inserir, buscar ou alterar.


#### 👾 Testes Exploratorios. 
 Para guiar os testes exploratorios foi usado uma euristica de testes conhecida como VADER (Veja mais [aqui](https://maximilianoalves.medium.com/vader-heuristica-para-teste-de-api-na-pratica-fcf78c6acec). 
  - [x] Regras de Negocio
  - [x] Continuidade de fluxos
  - [x] Passagem de parametros
  - [x] Tipagem de Dados
  - [x] Uso de diferentes tokens
  - [x] Validação de metodos
  - [x] Listagem de 0 ou 1
  - [x] Estrutura de resposta de produtos
  - [x] Codigo de estados http
  - [x] Documentação da API

Os testes exploratórios foram feitos diretamente no swagger e postman depois de adicionar as coleções no postman para realizar testes de carga. 
    
#### 📈Testes relacionados as regras de negócios
- [x] Executar as regras de negócios
- [x] Continuidade dos fluxos
- [x] Listagem de muitos registros
- [x] Mascaras, habilitação/desabilitação e tipagem dos campos
- [x] Variação de rede
- [x] Integração com API
- [x] Perda de foco da Aplicação

  
### 🛠️Criar testes funcionais baseado em regra de negócio 
Dentre as regras de negócio temos:
```
"Só é possível adicionar um produto que o valor esteja entre R$0,01 e R$7.000,00"
```
Então baseando no padrão **Gherkin** temos dois testes funcionais criados:  
1.
```
Dado o app lojinha instalado no device  
E o usuario acessa a pagina de login  
E preenche com suas credenciais validas  
E aciona a opção de realizar login  
Quando aciona o botão "+" para adicionar um produto  
E preenche com nome e valor acima de R$7.000,00  
Então é exibido um alerta de erro.  
```
2. 
```
Dado o app lojinha instalado no device  
E o usuario acessa a pagina de login  
E preenche com suas credenciais validas  
E aciona a opção de realizar login  
Quando aciona o botão "+" para adicionar um produto  
E preenche com nome e valor abaixo de R$0,01  
Então é exibido um alerta de erro.  
```


<img src="/  images/MensagemDeErro.png" height="300">

### 👨‍💻 Automatizar os casos de teste criados
Foi utilizada as bibliotecas do rest assured e do Junit junto com Selenium Webdriver. O padrão para a documentação utilizado foi o #PageObjects 

~~~java
@Test
    @DisplayName("Validar limite inferior do valor do produto")
    public void testValidarprecoZero() {
        given()
                .contentType(ContentType.JSON)
                .header("token", this.token)
                .body(ProdutodataFactory.criarProdutoComumIgualaoValorA(0.00))
        .when()
                    .post("/v2/produtos")
        .then()
                    .assertThat()
                        .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                        .statusCode(422);
    }

    @Test
    @DisplayName("Validar limite superior do valor do produto")
    public void testValidarPrecoAcima() {
        given()
                .contentType(ContentType.JSON)
                .header("token", this.token)
                .body(ProdutodataFactory.criarProdutoComumIgualaoValorA(7001.00))
        .when()
                    .post("/v2/produtos")
        .then()
                    .assertThat()
                        .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                        .statusCode(422);
    }
~~~
Veja o código completo [aqui](https://github.com/mateusralv/lojinha-testes-api/blob/338a25455ce2cf259d0ac65a29508f8c2abb8bda/src/test/java/modulos/produto/ProdutoTest.java#L59C5-L72C6).
