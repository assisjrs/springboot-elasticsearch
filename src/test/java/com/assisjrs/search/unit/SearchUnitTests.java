package com.assisjrs.search.unit;

import com.assisjrs.search.ativo.SearchService;
import com.assisjrs.search.ativo.Search;
import com.assisjrs.search.ativo.SearchRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchUnitTests {

	@InjectMocks
	private SearchService service;

	@Mock
	private SearchRepository repository;

	@Test
	public void deveBuscarNoRepositorio(){
		final Search search = new Search();
		search.setId("1");
		search.setCodigoCetip("CSMGA1");
		search.setDescricao("xyz");

		when(repository.findByCodigoCetip("CSMGA1")).thenReturn(search);

		final Search searchEncontrada = service.findByCodigoCetip("CSMGA1");

		assertThat(searchEncontrada.getId()).isEqualTo("1");
	}
}
