package com.assisjrs.search.integration;

import com.assisjrs.search.ativo.Search;
import com.assisjrs.search.ativo.SimpleSearch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleSearchByDescricaoIntegrationTests {

	@Autowired
	private ElasticsearchTemplate template;

	@Autowired
	private SimpleSearch simpleSearch;

	@Before
	public void before() {
		template.deleteIndex(Search.class);
		template.createIndex(Search.class);
		template.putMapping(Search.class);
		template.refresh(Search.class);
	}

	@Test
	public void deveRetornarNullQuandoSemResultados(){

    }
}
