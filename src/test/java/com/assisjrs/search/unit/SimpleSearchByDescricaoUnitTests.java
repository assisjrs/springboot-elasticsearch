package com.assisjrs.search.unit;

import com.assisjrs.search.ativo.ResultsResponse;
import com.assisjrs.search.ativo.SimpleSearch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimpleSearchByDescricaoUnitTests {
	@InjectMocks
	private SimpleSearch search;

	@Mock
    private ElasticsearchTemplate template;

	@Test
	public void naoDeveRetornarNullQuandoSemResultados(){
        when(template.query(any(), any())).thenReturn(new ResultsResponse());
		final ResultsResponse results = search.by("NAO_EXISTE");

		assertThat(results).isNotNull();
	}

    @Test
    public void deveRetornarFoundComoFalseQuandoSemResultados(){
	    final ResultsResponse response = new ResultsResponse();

        response.setFound(false);

        when(template.query(any(), any())).thenReturn(response);
        final ResultsResponse results = search.by("NAO_EXISTE");

        assertThat(results.isFound()).isFalse();
    }

    @Test
    public void deveRetornarTook(){
        when(template.query(any(), any())).thenReturn(new ResultsResponse());

        final ResultsResponse results = search.by("EPL4");

        assertThat(results.getTook()).isEqualTo(1000L);
    }
}
