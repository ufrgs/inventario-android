package br.ufrgs.cpd.inventario.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hermes on 29/08/2018
 */

public class ListPessoas {
    @SerializedName("servidores")
    @Expose
    public List<Pessoa> pessoasList = null;
}
