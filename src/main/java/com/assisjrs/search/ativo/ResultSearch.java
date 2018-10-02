package com.assisjrs.search.ativo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.search.highlight.HighlightField;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResultSearch {
    private Float score;
    private List<HighlightField> highlightFields;//?

    private String titulo;
    private String descricao;

    private Search search;
}
