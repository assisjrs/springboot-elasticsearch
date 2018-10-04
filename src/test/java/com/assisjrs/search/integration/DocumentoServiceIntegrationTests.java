package com.assisjrs.search.integration;

import com.assisjrs.search.ativo.Documento;
import com.assisjrs.search.ativo.DocumentoService;
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
public class DocumentoServiceIntegrationTests {

	@Autowired
	private ElasticsearchTemplate template;

    @Autowired
    private DocumentoService service;

	@Before
	public void before() {
		template.deleteIndex(Documento.class);
		template.createIndex(Documento.class);
		template.putMapping(Documento.class);
		template.refresh(Documento.class);
	}

    @Test
    public void deveExistirOIndice() {
        assertThat(template.typeExists("data", "search")).isTrue();
    }

	@Test
	public void deveRetornarNullQuandoSemResultados(){
		final Documento documento = service.findByCodigo("NAO_EXISTE");

		assertThat(documento).isNull();
	}

	@Test
	public void deveSalvarUmItem(){
		final Documento documento = new Documento();

		documento.setId("666");
		documento.setCodigo("ELPLA1");
		documento.setDescricao("xyz");


		final Documento found = service.save(documento);

		assertThat(found.getId()).isEqualTo("666");
	}

	@Test
	public void deveBuscarPeloCodigo(){
		final Documento documento = new Documento();

		documento.setId("333");
		documento.setCodigo("ELPLA1");
		documento.setDescricao("xyz");

		service.save(documento);

		final Documento found = service.findByCodigo("ELPLA1");

		assertThat(found.getCodigo()).isEqualTo("ELPLA1");
	}
}
