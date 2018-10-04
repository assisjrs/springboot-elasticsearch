package com.assisjrs.search.integration;

import com.assisjrs.search.ativo.Documento;
import com.assisjrs.search.ativo.ResultsResponse;
import com.assisjrs.search.ativo.DocumentoRepository;
import com.assisjrs.search.ativo.SimpleSearch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleSearchIntegrationTests {

	@Autowired
	private ElasticsearchTemplate template;

	@Autowired
	private DocumentoRepository repository;

	@Autowired
	private SimpleSearch search;

	@Before
	public void before() {
		template.deleteIndex(Documento.class);
		template.createIndex(Documento.class);
		template.putMapping(Documento.class);
		template.refresh(Documento.class);
	}

	@Test
	public void naoDeveRetornarNullQuandoSemResultados(){
		final ResultsResponse results = search.by("NAO_EXISTE");

		assertThat(results).isNotNull();
	}

	@Test
	public void deveRetornarFoundComoFalseQuandoSemResultados(){
		final ResultsResponse results = search.by("NAO_EXISTE");

		assertThat(results.isFound()).isFalse();
	}

	@Test
	public void deveRetornarTook(){
        final Documento documento = new Documento();
        documento.setId("1");
        documento.setCodigo("ELPL4");
        documento.setDescricao("XYZ ELPL4 ABC");

	    repository.save(documento);

		final ResultsResponse results = search.by("ELPL4");

		assertThat(results.getTook()).isNotNull();
	}

    @Test
    public void deveRetornarOsDocumentos(){
        final Documento documento1 = new Documento();
        documento1.setId("1");
        documento1.setCodigo("ELPL4");
        documento1.setDescricao("XYZ ELPL4 ABC");

        repository.save(documento1);

        final Documento documento2 = new Documento();
        documento2.setId("2");
        documento2.setCodigo("ELPL4");
        documento2.setDescricao("XYZ ELPL4 2 ABC");

        repository.save(documento2);

        final ResultsResponse results = search.by("ELPL4");

        assertThat(results.getResults().size()).isEqualTo(2);
    }

    @Test
    public void deveRetornarODocumentoInserido(){
        final Documento documento = new Documento();
        documento.setId("1");
        documento.setCodigo("ELPL4");
        documento.setDescricao("XYZ ELPL4 ABC");

        repository.save(documento);

        final ResultsResponse results = search.by("ELPL4");

        assertThat(results.getResults().get(0).getDocumento()).isNotNull();
    }
}
