package com.assisjrs.search.ativo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.search.highlight.HighlightField;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResultSearch {
    private Float score;
    private Map<String, HighlightField> highlightFields;

    private String titulo;
    private String descricao;

    private Search search;

    public Map<String, HighlightField> getHighlightFields(){
        if(highlightFields == null) highlightFields = new HashMap<>();
        return highlightFields;
    }
}
