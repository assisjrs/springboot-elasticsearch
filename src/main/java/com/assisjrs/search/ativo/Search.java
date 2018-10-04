package com.assisjrs.search.ativo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "data", type = "search")
@Data
public class Search {
    @Id
    private String id;
    private String descricao;

    private String codigo;
}
