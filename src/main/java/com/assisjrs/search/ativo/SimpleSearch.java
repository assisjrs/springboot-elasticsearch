package com.assisjrs.search.ativo;

import org.elasticsearch.search.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Component
public class SimpleSearch {
    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private SimpleSearchResultsExtractor resultsExtractor;

    public ResultsResponse by(final String q) {
        final SearchQuery query = new NativeSearchQueryBuilder()
                                                    .withQuery(matchQuery("descricao", q))
                                                    .withHighlightFields(new HighlightBuilder.Field("descricao"))
                                                    .build();

        return template.query(query, resultsExtractor);
    }
}
