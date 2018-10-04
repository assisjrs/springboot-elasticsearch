package com.assisjrs.search.ativo;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoRepository extends ElasticsearchRepository<Documento, String> {
    Documento findByCodigo(String codigo);
}
