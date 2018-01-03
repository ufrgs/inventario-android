package br.ufrgs.cpd.inventario.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Theo on 03/10/2017.
 */

public class EspacosFisico implements Serializable {
    @SerializedName("CodEspacoFisico")
    @Expose
    public String codEspacoFisico;
    @SerializedName("DenominacaoEspacoFisico")
    @Expose
    public String denominacaoEspacoFisico;
    @SerializedName("CodEspacoFisicoSuperior")
    @Expose
    public String codEspacoFisicoSuperior;
    @SerializedName("CodSemantico")
    @Expose
    public String codSemantico;
}
