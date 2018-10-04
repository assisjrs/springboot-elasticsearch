package com.assisjrs.search.ativo;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SimpleSearchResultsExtractor implements ResultsExtractor<ResultsResponse> {
    @Override
    public ResultsResponse extract(final SearchResponse response) {
        final ResultsResponse results = new ResultsResponse();

        if(response == null) return results;

        results.setTook(response.getTookInMillis());

        final SearchHits hits = response.getHits();

        if(hits != null) {
            results.setTotalHits(hits.getTotalHits());
            results.setMaxScore(hits.getMaxScore());
            results.setFound(hits.getTotalHits() > 0L);

            if(hits.getHits() != null)
                for (final SearchHit hit: hits.getHits()) {
                    final ResultSearch resultSearch = new ResultSearch();

                    resultSearch.setScore(hit.getScore());
                    resultSearch.setDescricao(hit.getSourceAsString());

                    final Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                    final HighlightField field = highlightFields.get("descricao");
                    resultSearch.setHighlight("");

                    if(field != null) {
                        for (final Text fragment : field.getFragments()) {
                            resultSearch.setHighlight(fragment.string());
                        }
                    }

                    final Documento documento = new Documento();
                    documento.setId((String) hit.getSource().get("id"));
                    documento.setCodigo((String) hit.getSource().get("codigo"));
                    documento.setDescricao((String) hit.getSource().get("descricao"));

                    resultSearch.setDocumento(documento);

                    results.getResults().add(resultSearch);
                }
        }

        return results;
    }
}
