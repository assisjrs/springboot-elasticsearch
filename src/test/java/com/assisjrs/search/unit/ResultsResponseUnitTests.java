package com.assisjrs.search.unit;

import com.assisjrs.search.ativo.ResultsResponse;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResultsResponseUnitTests {

    @Test
	public void naoDeveRetornarUmaListaNula(){
		final ResultsResponse searchs = new ResultsResponse();

		assertThat(searchs.getResults()).isNotNull();
	}

    @Test
    public void naoDeveRetornarUmaListaNulaMesmoAtribuindoNulo(){
        final ResultsResponse searchs = new ResultsResponse();

        searchs.setResults(null);

        assertThat(searchs.getResults()).isNotNull();
    }
}
