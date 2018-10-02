package com.assisjrs.search.ativo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search/")
public class SearchResource {
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public ResultsResponse mainSearch(final @RequestBody SearchParams q){
        final ResultsResponse response = new ResultsResponse();

        if ("NAO_TEM".equals(q.getText()))
            return response;

        final Search search = new Search();

        search.setId("1");
        search.setCodigoCetip("CSMGA1");
        search.setDescricao("xyz");

        response.getResults().add(new ResultSearch(null, null, null, null, search));

        return response;
    }
}
