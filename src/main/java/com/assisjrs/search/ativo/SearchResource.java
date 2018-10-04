package com.assisjrs.search.ativo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search/")
public class SearchResource {
    @Autowired
    private SimpleSearch simpleSearch;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public ResultsResponse search(final @RequestBody SearchParams q){
        return simpleSearch.by(q.getText());
    }
}
