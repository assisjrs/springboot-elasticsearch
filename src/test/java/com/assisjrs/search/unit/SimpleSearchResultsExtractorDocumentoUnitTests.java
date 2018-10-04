package com.assisjrs.search.unit;

import com.assisjrs.search.ativo.ResultsResponse;
import com.assisjrs.search.ativo.SimpleSearchResultsExtractor;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
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
public class SimpleSearchResultsExtractorDocumentoUnitTests {
    @InjectMocks
    private SimpleSearchResultsExtractor resultsExtractor;

    @Mock
    private SearchResponse response;

   	@Test
    public void deveRetornarODocumentos(){
        final SearchHits hits = mock(SearchHits.class);
        when(response.getHits()).thenReturn(hits);

        final SearchHit hit = mock(SearchHit.class);
        when(hit.getSource()).thenReturn(new HashMap<>());

        final SearchHit[] hitList = new SearchHit[]{hit};
        when(hits.getHits()).thenReturn(hitList);

        final ResultsResponse resultSearch = resultsExtractor.extract(response);

        assertThat(resultSearch.getResults().get(0).getDocumento()).isNotNull();
    }

    @Test
    public void deveRetornarOIdDoDocumentos(){
        final SearchHits hits = mock(SearchHits.class);
        when(response.getHits()).thenReturn(hits);

        final Map<String, Object> docFields = new HashMap<>();

        docFields.put("id", "1");

        final SearchHit hit = mock(SearchHit.class);
        when(hit.getSource()).thenReturn(docFields);

        final SearchHit[] hitList = new SearchHit[]{hit};
        when(hits.getHits()).thenReturn(hitList);

        final ResultsResponse resultSearch = resultsExtractor.extract(response);

        assertThat(resultSearch.getResults()
                               .get(0)
                               .getDocumento()
                               .getId()).isEqualTo("1");
    }

    @Test
    public void deveRetornarOCodigoDoDocumentos(){
        final SearchHits hits = mock(SearchHits.class);
        when(response.getHits()).thenReturn(hits);

        final Map<String, Object> docFields = new HashMap<>();

        docFields.put("codigo", "ELPL4");

        final SearchHit hit = mock(SearchHit.class);
        when(hit.getSource()).thenReturn(docFields);

        final SearchHit[] hitList = new SearchHit[]{hit};
        when(hits.getHits()).thenReturn(hitList);

        final ResultsResponse resultSearch = resultsExtractor.extract(response);

        assertThat(resultSearch.getResults()
                               .get(0)
                               .getDocumento()
                               .getCodigo()).isEqualTo("ELPL4");
    }

    @Test
    public void deveRetornarADescricaoDoDocumentos(){
        final SearchHits hits = mock(SearchHits.class);
        when(response.getHits()).thenReturn(hits);

        final Map<String, Object> docFields = new HashMap<>();

        docFields.put("descricao", "ELPL4 vencimento 24/01/2019");

        final SearchHit hit = mock(SearchHit.class);
        when(hit.getSource()).thenReturn(docFields);

        final SearchHit[] hitList = new SearchHit[]{hit};
        when(hits.getHits()).thenReturn(hitList);

        final ResultsResponse resultSearch = resultsExtractor.extract(response);

        assertThat(resultSearch.getResults()
                               .get(0)
                               .getDocumento()
                               .getDescricao()).isEqualTo("ELPL4 vencimento 24/01/2019");
    }
}
