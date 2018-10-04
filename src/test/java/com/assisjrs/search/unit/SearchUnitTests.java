package com.assisjrs.search.unit;

import com.assisjrs.search.ativo.Documento;
import com.assisjrs.search.ativo.DocumentoService;
import com.assisjrs.search.ativo.DocumentoRepository;
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
	private DocumentoService service;

	@Mock
	private DocumentoRepository repository;

	@Test
	public void deveBuscarNoRepositorio(){
		final Documento documento = new Documento();
		documento.setId("1");
		documento.setCodigo("CSMGA1");
		documento.setDescricao("xyz");

		when(repository.findByCodigo("CSMGA1")).thenReturn(documento);

		final Documento documentoEncontrado = service.findByCodigo("CSMGA1");

		assertThat(documentoEncontrado.getId()).isEqualTo("1");
	}
}
