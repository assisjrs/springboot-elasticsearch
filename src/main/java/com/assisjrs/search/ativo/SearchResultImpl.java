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
    public <T> AggregatedPage<T> mapResults(final SearchResponse response, final Class<T> clazz, final Pageable pageable) {
        final List<Documento> documentos = new ArrayList<>();

        for (final SearchHit hit : response.getHits()) {
            if (response.getHits().getHits().length <= 0) return null;

            final Documento documento = new Documento();

            documento.setId(hit.getId());
            documento.setCodigo((String)hit.getSource().get("codigo"));
            documento.setDescricao((String)hit.getSource().get("descricao"));
            documentos.add(documento);
        }

        return new AggregatedPageImpl(documentos);
    }
}
