package br.ufrgs.cpd.inventario.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Theo on 13/10/17.
 */

public class EstadosConservacao {

    @SerializedName("DescricaoEstadoConservacao")
    @Expose
    public String descricaoEstadoConservacao;
    @SerializedName("TipoEstadoConservacao")
    @Expose
    public String tipoEstadoConservacao;

}
