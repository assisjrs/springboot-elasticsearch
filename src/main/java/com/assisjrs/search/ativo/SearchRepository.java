package com.assisjrs.search.ativo;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends ElasticsearchRepository<Search, String> {
    Search findByCodigoCetip(String codigoCetip);
}
