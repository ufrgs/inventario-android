package br.ufrgs.cpd.inventario.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Theo on 29/09/17.
 */

public class Predio implements Serializable {
    @SerializedName("CodPredio")
    @Expose
    public String CodPredio;
    @SerializedName("NomePredio")
    @Expose
    public String NomePredio;
    @SerializedName("CodEspacoFisico")
    @Expose
    public String CodEspacoFisico;
    @SerializedName("CodSemantico")
    @Expose
    public String CodSemantico;
}
