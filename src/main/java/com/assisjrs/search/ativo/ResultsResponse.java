package com.assisjrs.search.ativo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResultsResponse {
    private Long took;
    private Long totalHits;
    private Float maxScore;
    private boolean found;

    private List<ResultSearch> results;

    public List<ResultSearch> getResults(){
        if(results == null) results = new ArrayList<>();

        return results;
    }
}
