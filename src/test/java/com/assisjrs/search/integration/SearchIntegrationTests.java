package com.assisjrs.search.integration;

import com.assisjrs.search.ativo.Documento;
import com.assisjrs.search.ativo.SearchResultImpl;
import com.assisjrs.search.ativo.DocumentoService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.search.highlight.HighlightBuilder.Field;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchIntegrationTests {

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
    public void deveBuscarPeloCodigoCetip(){
        final Documento documento = new Documento();

        documento.setId("333");
        documento.setCodigo("ELPLA1");
        documento.setDescricao("xyz");

        service.save(documento);

        final Documento found = service.findByCodigo("ELPLA1");

        assertThat(found.getCodigo()).isEqualTo("ELPLA1");
    }

    @Test
    public void exemploScroll(){
        final Documento documento = new Documento();

        documento.setId(randomNumeric(5));
        documento.setCodigo("ELPLA1");
        documento.setDescricao("some test message");

        final IndexQuery indexQuery = getIndexQuery(documento);

        template.index(indexQuery);
        template.refresh(Documento.class);

        final SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("descricao", "test"))
                //.withIndices("data")
                //.withTypes("search")
                .withPageable(new PageRequest(0,10))
                .build();

        final String scrollId = template.scan(searchQuery,1000,false);
        final List<Documento> searches = new ArrayList<>();

        boolean hasRecords = true;

        while (hasRecords) {
            final Page<Documento> page = template.scroll(scrollId, 5000L, new SearchResultImpl());

            if(page != null) {
                searches.addAll(page.getContent());
                hasRecords = page.hasNext();
            }
            else{
                hasRecords = false;
            }
        }

        assertThat(searches).isNotEmpty();
    }

    @Test
    public void exemploBuscaHighLightDescricaoNormal(){
        final Documento documento = new Documento();

        documento.setId(randomNumeric(5));
        documento.setCodigo("ELPLA1");
        documento.setDescricao("some test message");

        final IndexQuery indexQuery = getIndexQuery(documento);

        template.index(indexQuery);
        template.refresh(Documento.class);
        service.save(documento);

        final SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("descricao", "test"))
                //.withIndices("data")
                //.withTypes("search")
                //.withPageable(new PageRequest(0,10))
                .withHighlightFields(new Field("descricao")
                        .preTags("<mark>")
                        .postTags("</mark>"))
                .build();

        final Page<Documento> page = template.queryForPage(searchQuery, Documento.class, new SearchResultImpl());

        assertThat(page.getContent().get(0).getDescricao()).hasToString("some test message");
    }

    @Test
    public void exemploBuscaHighLight(){
        final Documento documento = new Documento();

        documento.setId(randomNumeric(5));
        documento.setCodigo("ELPLA1");
        documento.setDescricao("some test message");

        final IndexQuery indexQuery = getIndexQuery(documento);

        template.index(indexQuery);
        template.refresh(Documento.class);
        service.save(documento);

        final SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("descricao", "test"))
                //.withIndices("data")
                //.withTypes("search")
                //.withPageable(new PageRequest(0,10))
                .withHighlightFields(new Field("descricao")
                        .preTags("<mark>")
                        .postTags("</mark>"))
                .build();

        final Page<Documento> page = template.queryForPage(searchQuery, Documento.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                final List<Documento> documentos = new ArrayList<>();

                for (final SearchHit hit : response.getHits()) {
                    if (response.getHits().getHits().length <= 0) return null;

                    final Documento doc = new Documento();

                    doc.setId(hit.getId());
                    doc.setCodigo((String)hit.getSource().get("codigo"));
                    doc.setDescricao(hit.getHighlightFields().get("descricao").fragments()[0].toString());
                    documentos.add(doc);
                }

                return new AggregatedPageImpl(documentos);
            }
        });

        assertThat(page.getContent().get(0).getDescricao()).hasToString("some <mark>test</mark> message");
    }

    private IndexQuery getIndexQuery(Documento sampleEntity) {
        return new IndexQueryBuilder()
                .withId(sampleEntity.getId())
                .withObject(sampleEntity)
                //.withVersion(sampleEntity.getVersion())
                .build();
    }
}
