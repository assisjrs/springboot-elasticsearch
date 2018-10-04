package com.assisjrs.search.unit;

import com.assisjrs.search.ativo.ResultsResponse;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResultsResponseUnitTests {

    @Test
	public void naoDeveRetornarUmaListaNula(){
		final ResultsResponse response = new ResultsResponse();

		assertThat(response.getResults()).isNotNull();
	}

    @Test
    public void naoDeveRetornarUmaListaNulaMesmoAtribuindoNulo(){
        final ResultsResponse response = new ResultsResponse();

        response.setResults(null);

        assertThat(response.getResults()).isNotNull();
    }
}
