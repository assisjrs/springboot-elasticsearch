package com.assisjrs.search.unit;

import com.assisjrs.search.ativo.ResultSearch;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResultSearchUnitTests {
	@Test
	public void naoDeveRetornarUmaListaNula(){
		final ResultSearch result = new ResultSearch();

		assertThat(result.getHighlightFields()).isNotNull();
	}

	@Test
	public void naoDeveRetornarUmaListaNulaMesmoAtribuindoNulo(){
		final ResultSearch result = new ResultSearch();

		result.setHighlightFields(null);

		assertThat(result.getHighlightFields()).isNotNull();
	}
}
