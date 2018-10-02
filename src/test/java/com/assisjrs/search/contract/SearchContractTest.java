package com.assisjrs.search.contract;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SearchContractTest {
    @Value("${local.server.port:8080}")
    private int port = 0;

    protected String url(final String context){
        return "http://localhost:" + port + context;
    }

    @Test
    public void deveRetornar200QuandoNaoEncontrarResultado(){
        given().contentType("application/json")
               .body("{\"text\": \"NAO_TEM\"}")
               .post(url("/search/"))
       .then()
       .statusCode(OK.value());
    }

    @Test
    public void quandoNaoEncontrarResultadoDeveRetornarUmObjetoResultsResponse(){
        final String results =
                given().contentType("application/json")
                        .body("{\"text\": \"NAO_TEM\"}")
                        .post(url("/search/"))
                        .then()
                        .extract().jsonPath().getString("results");

        assertThat(results).isNotEmpty();
    }

    @Test
    public void quandoNaoEncontrarResultadoDeveRetornarFalseNoFound(){
        final Boolean found =
                given().contentType("application/json")
                        .body("{\"text\": \"NAO_TEM\"}")
                        .post(url("/search/"))
                        .then()
                        .extract().jsonPath().getBoolean("found");

        assertThat(found).isFalse();
    }

    @Test
    public void deveRetornar200QuandoExistir() {
        given().contentType("application/json")
               .body("{\"text\": \"CSMGA1\"}")
               .post(url("/search/"))
       .then()
       .statusCode(OK.value());
    }

    @Test
    public void deveRetornarOId() {
        final String id =
                given().contentType("application/json")
                       .body("{\"text\": \"CSMGA1\"}")
                       .post(url("/search/"))
               .then()
               .extract().jsonPath().getString("results[0].search.id");

        assertThat(id).isEqualTo("1");
    }

    @Test
    public void deveRetornarOCodigoCetip() {
        final String codigoCetip =
                given().contentType("application/json")
                       .body("{\"text\": \"CSMGA1\"}")
                       .post(url("/search/"))
                .then()
                .extract().jsonPath().getString("results[0].search.codigoCetip");

        assertThat(codigoCetip).isEqualTo("CSMGA1");
    }

    @Test
    public void deveRetornarADescricao() {
        final String descricao =
                given().contentType("application/json")
                       .body("{\"text\": \"CSMGA1\"}")
                       .post(url("/search/"))
                .then()
                .extract().jsonPath().getString("results[0].search.descricao");

        assertThat(descricao).isEqualTo("xyz");
    }
}
