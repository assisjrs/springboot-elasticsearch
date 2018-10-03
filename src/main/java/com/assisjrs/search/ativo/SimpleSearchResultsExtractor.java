package com.assisjrs.search.ativo;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.stereotype.Component;

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
                    resultSearch.setHighlightFields(hit.getHighlightFields());

                    results.getResults().add(resultSearch);
                }
        }

        return results;
    }
}
