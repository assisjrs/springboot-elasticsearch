package com.assisjrs.search.contract;

import com.assisjrs.search.ativo.Search;
import com.assisjrs.search.ativo.SearchRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SearchContractTest {
    @Value("${local.server.port:8080}")
    private int port = 0;

    protected String url(final String context){
        return "http://localhost:" + port + context;
    }

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private SearchRepository repository;

    @Before
    public void before() {
        template.deleteIndex(Search.class);
        template.createIndex(Search.class);
        template.putMapping(Search.class);
        template.refresh(Search.class);

        final Search s = new Search();
        s.setId("1");
        s.setCodigo("ELPL4");
        s.setDescricao("XYZ ELPL4 ABC");

        repository.save(s);
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
               .body("{\"text\": \"ELPL4\"}")
               .post(url("/search/"))
       .then()
       .statusCode(OK.value());
    }

    @Test
    public void deveRetornarOId() {
        final String id =
                given().contentType("application/json")
                       .body("{\"text\": \"ELPL4\"}")
                       .post(url("/search/"))
               .then()
               .extract().jsonPath().getString("results[0].search.id");

        assertThat(id).isEqualTo("1");
    }

    @Test
    public void deveRetornarOCodigo() {
        final String codigo =
                given().contentType("application/json")
                       .body("{\"text\": \"ELPL4\"}")
                       .post(url("/search/"))
                .then()
                .extract().jsonPath().getString("results[0].search.codigo");

        assertThat(codigo).isEqualTo("ELPL4");
    }

    @Test
    public void deveRetornarADescricao() {
        final String descricao =
                given().contentType("application/json")
                       .body("{\"text\": \"ELPL4\"}")
                       .post(url("/search/"))
                .then()
                .extract().jsonPath().getString("results[0].search.descricao");

        assertThat(descricao).isEqualTo("XYZ ELPL4 ABC");
    }
}
