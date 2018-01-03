package br.ufrgs.cpd.inventario.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Theo on 29/09/17.
 */

public class Orgao implements Serializable {
    @SerializedName("CodOrgao")
    @Expose
    public String CodOrgao;
    @SerializedName("NomeOrgao")
    @Expose
    public String NomeOrgao;
    @SerializedName("SiglaOrgao")
    @Expose
    public String SiglaOrgao;
}
