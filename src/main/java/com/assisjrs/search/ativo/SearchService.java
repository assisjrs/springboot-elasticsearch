package com.assisjrs.search.ativo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    @Autowired
    private SearchRepository repository;

    public Search findByCodigo(String codigo) {
        return repository.findByCodigo(codigo);
    }

    public Search salvar(Search s) {
        return repository.save(s);
    }
}
