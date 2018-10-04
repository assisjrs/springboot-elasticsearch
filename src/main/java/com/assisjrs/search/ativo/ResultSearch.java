package com.assisjrs.search.ativo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResultSearch {
    private Float score;

    private String titulo;
    private String descricao;
    private String highlight;

    private Documento documento;
}
