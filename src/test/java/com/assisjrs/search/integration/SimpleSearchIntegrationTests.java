package com.assisjrs.search.integration;

import com.assisjrs.search.ativo.ResultsResponse;
import com.assisjrs.search.ativo.Search;
import com.assisjrs.search.ativo.SearchRepository;
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
	private SearchRepository repository;

	@Autowired
	private SimpleSearch search;

	@Before
	public void before() {
		template.deleteIndex(Search.class);
		template.createIndex(Search.class);
		template.putMapping(Search.class);
		template.refresh(Search.class);
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
        final Search s = new Search();
        s.setId("1");
        s.setCodigo("ELPL4");
        s.setDescricao("XYZ ELPL4 ABC");

	    repository.save(s);

		final ResultsResponse results = search.by("EPL4");

		assertThat(results.getTook()).isNotNull();
	}
}
