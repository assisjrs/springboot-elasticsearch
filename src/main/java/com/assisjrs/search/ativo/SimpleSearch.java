package com.assisjrs.search.ativo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Component;

@Component
public class SimpleSearch {
    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private SimpleSearchResultsExtractor resultsExtractor;

    public ResultsResponse by(final String q) {
        final ResultsResponse results = template.query(null, resultsExtractor);

        results.setFound(results != null);

        return results;
    }
}
