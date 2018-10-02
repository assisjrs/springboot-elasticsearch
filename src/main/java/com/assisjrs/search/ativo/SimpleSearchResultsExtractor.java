package com.assisjrs.search.ativo;

import org.elasticsearch.action.search.SearchResponse;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.stereotype.Component;

@Component
public class SimpleSearchResultsExtractor implements ResultsExtractor<ResultSearch> {
    @Override
    public ResultSearch extract(final SearchResponse response) {
        return null;
    }
}
