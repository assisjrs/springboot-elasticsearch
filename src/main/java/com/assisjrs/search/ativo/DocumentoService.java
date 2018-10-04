package com.assisjrs.search.ativo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepository repository;

    public Documento findByCodigo(final String codigo) {
        return repository.findByCodigo(codigo);
    }

    public Documento save(final Documento s) {
        return repository.save(s);
    }
}
