package com.assisjrs.search.unit;

import com.assisjrs.search.ativo.ResultsResponse;
import com.assisjrs.search.ativo.SimpleSearchResultsExtractor;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimpleSearchResultsExtractorUnitTests {
	@InjectMocks
	private SimpleSearchResultsExtractor resultsExtractor;

	@Mock
	private SearchResponse response;

    @Test
    public void naoDeveRetornarNull(){
        assertThat(resultsExtractor.extract(null)).isNotNull();
    }

	@Test
	public void deveRetornarTook(){
		when(response.getTookInMillis()).thenReturn(1000L);

        final ResultsResponse resultSearch = resultsExtractor.extract(response);

        assertThat(resultSearch.getTook()).isEqualTo(1000L);
	}

    @Test
    public void deveRetornarTotalHits(){
        final SearchHits hits = mock(SearchHits.class);
        when(response.getHits()).thenReturn(hits);
        when(hits.getTotalHits()).thenReturn(666L);

        final ResultsResponse resultSearch = resultsExtractor.extract(response);

        assertThat(resultSearch.getTotalHits()).isEqualTo(666L);
    }

    @Test
    public void deveRetornarMaxScore(){
        final SearchHits hits = mock(SearchHits.class);
        when(response.getHits()).thenReturn(hits);
        when(hits.getMaxScore()).thenReturn(10.22F);

        final ResultsResponse resultSearch = resultsExtractor.extract(response);

        assertThat(resultSearch.getMaxScore()).isEqualTo(10.22F);
    }

    @Test
    public void deveRetornarResultsMesmoSemResultados(){
        final SearchHits hits = mock(SearchHits.class);
        when(response.getHits()).thenReturn(hits);
        when(hits.getHits()).thenReturn(null);

        final ResultsResponse resultSearch = resultsExtractor.extract(response);

        assertThat(resultSearch.getResults()).isNotNull();
    }

    @Test
    public void deveRetornarResultsMesmoAMesmaQuantidadeDeResultados(){
        final SearchHits hits = mock(SearchHits.class);
        when(response.getHits()).thenReturn(hits);

        final SearchHit[] hitList = new SearchHit[]{mock(SearchHit.class), mock(SearchHit.class)};
        when(hits.getHits()).thenReturn(hitList);

        final ResultsResponse resultSearch = resultsExtractor.extract(response);

        assertThat(resultSearch.getResults().size()).isEqualTo(2);
    }

    @Test
    public void deveRetornarOScore(){
        final SearchHits hits = mock(SearchHits.class);
        when(response.getHits()).thenReturn(hits);

        final SearchHit hit = mock(SearchHit.class);
        when(hit.getScore()).thenReturn(0.17F);

        final SearchHit[] hitList = new SearchHit[]{hit};
        when(hits.getHits()).thenReturn(hitList);

        final ResultsResponse resultSearch = resultsExtractor.extract(response);

        assertThat(resultSearch.getResults().get(0).getScore()).isEqualTo(0.17F);
    }

    @Test
    public void deveRetornarOHighlightFields(){
        final SearchHits hits = mock(SearchHits.class);
        when(response.getHits()).thenReturn(hits);

        final SearchHit hit = mock(SearchHit.class);
        when(hit.getHighlightFields()).thenReturn(new HashMap<>());

        final SearchHit[] hitList = new SearchHit[]{hit};
        when(hits.getHits()).thenReturn(hitList);

        final ResultsResponse resultSearch = resultsExtractor.extract(response);

        assertThat(resultSearch.getResults().get(0).getHighlightFields()).isNotNull();
    }

    @Test
    public void deveRetornarTrazerAQuantidadeDeItensDoHighlightFields(){
        final SearchHits hits = mock(SearchHits.class);
        when(response.getHits()).thenReturn(hits);

        final SearchHit hit = mock(SearchHit.class);
        final Map<String, HighlightField> highlightFields = new HashMap<>();

        highlightFields.put("campo01", new HighlightField("campo01", new Text[]{}));

        when(hit.getHighlightFields()).thenReturn(highlightFields);

        final SearchHit[] hitList = new SearchHit[]{hit};
        when(hits.getHits()).thenReturn(hitList);

        final ResultsResponse resultSearch = resultsExtractor.extract(response);

        assertThat(resultSearch.getResults().get(0).getHighlightFields()).isEqualTo(highlightFields);
    }

    @Test
    public void deveRetornarFoundComoTrueQuandoComResultados(){
        final SearchHits hits = mock(SearchHits.class);
        when(response.getHits()).thenReturn(hits);

        when(hits.getTotalHits()).thenReturn(1000L);

        final ResultsResponse resultSearch = resultsExtractor.extract(response);

        assertThat(resultSearch.isFound()).isTrue();
    }
}
