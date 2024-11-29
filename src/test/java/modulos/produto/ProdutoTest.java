package modulos.produto;

import dataFactory.ProdutodataFactory;
import dataFactory.UsuarioDataFactory;
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

        //Obter o token do usuario
        this.token = given()
                .contentType(ContentType.JSON)
                .body(UsuarioDataFactory.criarUsuarioAdm())
            .when()
                .post("/v2/login")
            .then()
                .extract()
                    .path("data.token");
        System.out.println(token);
    }

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

    @Test
    @DisplayName("Delete um usuario")
    public void testDeleteUsuario() {
        given()
                .contentType(ContentType.JSON)
                .header("token", this.token)
        .when()
                    .delete("/v2/dados")
        .then()
                    .assertThat()
                    .statusCode(204);
    }

    @Test
    @DisplayName("Retorne todos os produtos de um usuario")
    public void testRetorneProdutosUsuario() {
        given()
                .contentType(ContentType.JSON)
                .header("token", this.token)
        .when()
                .get("/v2/produtos")
        .then()
                .extract()
                .path("data.produtoNome");
        System.out.println(token);
    }
}