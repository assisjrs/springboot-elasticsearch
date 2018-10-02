package com.assisjrs.search.integration;

import com.assisjrs.search.ativo.Search;
import com.assisjrs.search.ativo.SearchResultImpl;
import com.assisjrs.search.ativo.SearchService;
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
	private SearchService service;

	@Before
	public void before() {
		template.deleteIndex(Search.class);
		template.createIndex(Search.class);
		template.putMapping(Search.class);
		template.refresh(Search.class);
	}

	@Test
	public void deveRetornarNullQuandoSemResultados(){
        final Search search = service.findByCodigoCetip("NAO_EXISTE");

        assertThat(search).isNull();
    }

    @Test
    public void deveSalvarUmItem(){
	    final Search s = new Search();

	    s.setId("666");
	    s.setCodigoCetip("ELPLA1");
	    s.setDescricao("xyz");


        final Search found = service.salvar(s);

        assertThat(found.getId()).isEqualTo("666");
    }

    @Test
    public void deveBuscarPeloCodigoCetip(){
        final Search s = new Search();

        s.setId("333");
        s.setCodigoCetip("ELPLA1");
        s.setDescricao("xyz");

        service.salvar(s);

        final Search found = service.findByCodigoCetip("ELPLA1");

        assertThat(found.getCodigoCetip()).isEqualTo("ELPLA1");
    }

    @Test
    public void exemploScroll(){
        final Search s = new Search();

        s.setId(randomNumeric(5));
        s.setCodigoCetip("ELPLA1");
        s.setDescricao("some test message");

        final IndexQuery indexQuery = getIndexQuery(s);

        template.index(indexQuery);
        template.refresh(Search.class);

        final SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("descricao", "test"))
                //.withIndices("data")
                //.withTypes("search")
                .withPageable(new PageRequest(0,10))
                .build();

        final String scrollId = template.scan(searchQuery,1000,false);
        final List<Search> searches = new ArrayList<>();

        boolean hasRecords = true;

        while (hasRecords) {
            final Page<Search> page = template.scroll(scrollId, 5000L, new SearchResultImpl());

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
        final Search s = new Search();

        s.setId(randomNumeric(5));
        s.setCodigoCetip("ELPLA1");
        s.setDescricao("some test message");

        final IndexQuery indexQuery = getIndexQuery(s);

        template.index(indexQuery);
        template.refresh(Search.class);
        service.salvar(s);

        final SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("descricao", "test"))
                //.withIndices("data")
                //.withTypes("search")
                //.withPageable(new PageRequest(0,10))
                .withHighlightFields(new Field("descricao")
                        .preTags("<mark>")
                        .postTags("</mark>"))
                .build();

        final Page<Search> page = template.queryForPage(searchQuery, Search.class, new SearchResultImpl());

        assertThat(page.getContent().get(0).getDescricao()).hasToString("some test message");
    }

    @Test
    public void exemploBuscaHighLight(){
        final Search s = new Search();

        s.setId(randomNumeric(5));
        s.setCodigoCetip("ELPLA1");
        s.setDescricao("some test message");

        final IndexQuery indexQuery = getIndexQuery(s);

        template.index(indexQuery);
        template.refresh(Search.class);
        service.salvar(s);

        final SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("descricao", "test"))
                //.withIndices("data")
                //.withTypes("search")
                //.withPageable(new PageRequest(0,10))
                .withHighlightFields(new Field("descricao")
                        .preTags("<mark>")
                        .postTags("</mark>"))
                .build();

        final Page<Search> page = template.queryForPage(searchQuery, Search.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                final List<Search> searches = new ArrayList<>();

                for (final SearchHit hit : response.getHits()) {
                    if (response.getHits().getHits().length <= 0) return null;

                    final Search search = new Search();

                    search.setId(hit.getId());
                    search.setCodigoCetip((String)hit.getSource().get("codigoCetip"));
                    search.setDescricao(hit.getHighlightFields().get("descricao").fragments()[0].toString());
                    searches.add(search);
                }

                return new AggregatedPageImpl(searches);
            }
        });

        assertThat(page.getContent().get(0).getDescricao()).hasToString("some <mark>test</mark> message");
    }

    private IndexQuery getIndexQuery(Search sampleEntity) {
        return new IndexQueryBuilder()
                .withId(sampleEntity.getId())
                .withObject(sampleEntity)
                //.withVersion(sampleEntity.getVersion())
                .build();
    }

}
