package com.assisjrs.search.ativo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@Document(indexName = "data", type = "search")
@Data
public class Search {
    @Id
    private String id;
    private String descricao;

    private String codigoCetip;

    @DateTimeFormat(iso = DATE)
    private Date vencimento;
    private String isin;
    private String setor;
    private String tipo;
    private Boolean precificada;
    private Empresa emissor;
    private Empresa coordenadorLider;
    private Empresa agenteFidunciario;
}
