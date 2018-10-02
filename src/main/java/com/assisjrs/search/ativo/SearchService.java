package com.assisjrs.search.ativo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    @Autowired
    private SearchRepository repository;

    public Search findByCodigoCetip(String codigoCetip) {
        return repository.findByCodigoCetip(codigoCetip);
    }

    public Search salvar(Search s) {
        return repository.save(s);
    }
}
