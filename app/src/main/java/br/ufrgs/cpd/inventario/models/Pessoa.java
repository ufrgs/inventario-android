package br.ufrgs.cpd.inventario.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Theo on 23/10/2017.
 */

public class Pessoa implements Serializable {
    @SerializedName("CodPessoa")
    @Expose
    public String CodPessoa;
    @SerializedName("NomePessoa")
    @Expose
    public String NomePessoa;
}
