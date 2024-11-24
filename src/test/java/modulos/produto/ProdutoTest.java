package modulos.produto;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.ComponentePojo;
import pojo.ProdutoPojo;
import pojo.UsuarioPojo;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Testes de API REST do modulo de produto")
public class ProdutoTest {
    private String token;

    @BeforeEach
    public void beforeEach() {
        //Configurar os dados da API
        baseURI = "http://165.227.93.41";
        basePath = "/lojinha";

        UsuarioPojo usuario = new UsuarioPojo();
        usuario.setUsuarioLogin("admin");
        usuario.setUsuarioSenha("admin");

        //Obter o token do usuario
        this.token = given()
                .contentType(ContentType.JSON)
                .body(usuario)
            .when()
                .post("/v2/login")
            .then()
                .extract()
                    .path("data.token");
        System.out.println(token);
    }

    //Inserir um produto com valor 00
    //Inserir um produto com valor 00
    @Test
    @DisplayName("Validar os limites proibidos do valor do produto")
    public void testValidarprecoZero() {

        ProdutoPojo produto = new ProdutoPojo();
        produto.setProdutoNome("playstationss");
        produto.setProdutoValor(0.00);
        List<String> cores = new ArrayList<>();
        cores.add("preto");
        cores.add("branco");

        produto.setProdutoCores(cores);
        produto.setProdutoUrlMock("");

        List<ComponentePojo> componentes = new ArrayList<>();

        ComponentePojo componente = new ComponentePojo();
        componente.setComponenteNome("Controle");
        componente.setComponenteQuantidade(1);
        componentes.add(componente);

        produto.setComponentes(componentes);

        given()
                .contentType(ContentType.JSON)
                .header("token", this.token)
                .body(produto)
                .when()
                    .post("/v2/produtos")
                .then()
                    .assertThat()
                        .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                        .statusCode(422);
    }

    @Test
    @DisplayName("Validar os limites proibidos do valor do produto")
    public void testValidarPrecoacima() {
        given()
                .contentType(ContentType.JSON)
                .header("token", this.token)
                .body("{\n" +
                        "  \"produtoNome\": \"ps5\",\n" +
                        "  \"produtoValor\": 70001,\n" +
                        "  \"produtoCores\": [\n" +
                        "    \"preto\", \"Branco\"\n" +
                        "  ],\n" +
                        "  \"produtoUrlMock\": \"string\",\n" +
                        "  \"componentes\": [\n" +
                        "    {\n" +
                        "      \"componenteNome\": \"Controle\",\n" +
                        "      \"componenteQuantidade\": 2\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"componenteNome\": \"Jogo\",\n" +
                        "      \"componenteQuantidade\": 1\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}")
                .when()
                    .post("/v2/produtos")
                .then()
                    .assertThat()
                        .body("error", equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                        .statusCode(422);
    }
}