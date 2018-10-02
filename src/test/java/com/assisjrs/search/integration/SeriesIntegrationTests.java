package com.assisjrs.search.integration;

import com.assisjrs.search.ativo.Search;
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
public class SeriesIntegrationTests {

	@Autowired
	private ElasticsearchTemplate template;

	@Before
	public void before() {
		template.deleteIndex(Search.class);
		template.createIndex(Search.class);
		template.putMapping(Search.class);
		template.refresh(Search.class);
	}

	@Test
	public void testSave() {
        assertThat(template.typeExists("data", "search")).isTrue();
    }
}
