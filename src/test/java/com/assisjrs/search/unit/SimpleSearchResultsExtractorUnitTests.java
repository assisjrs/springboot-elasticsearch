package com.assisjrs.search.unit;

import com.assisjrs.search.ativo.ResultSearch;
import com.assisjrs.search.ativo.SimpleSearchResultsExtractor;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleSearchResultsExtractorUnitTests {
	@Autowired
	private SimpleSearchResultsExtractor resultsExtractor;

	@Test
	public void deveRetornarTook(){
		final SearchResponse response = mock(SearchResponse.class);

		when(response.getTook()).thenReturn(new TimeValue(1000L));

        final ResultSearch resultSearch = resultsExtractor.extract(response);

        //assertThat(resultSearch.getTook()).isEqualTo(1000L);
	}
}
