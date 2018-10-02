package com.assisjrs.search.ativo;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;

import java.util.ArrayList;
import java.util.List;

public class SearchResultImpl implements SearchResultMapper {
    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
        final List<Search> searches = new ArrayList<>();

        for (final SearchHit hit : response.getHits()) {
            if (response.getHits().getHits().length <= 0) return null;

            final Search search = new Search();

            search.setId(hit.getId());
            search.setCodigoCetip((String)hit.getSource().get("codigoCetip"));
            search.setDescricao((String)hit.getSource().get("descricao"));
            searches.add(search);
        }

        return new AggregatedPageImpl(searches);
    }
}
