package br.ufrgs.cpd.inventario.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Theo on 13/10/17.
 */

public class EstadosConservacao implements Serializable{

    @SerializedName("DescricaoEstadoConservacao")
    @Expose
    public String descricaoEstadoConservacao;
    @SerializedName("TipoEstadoConservacao")
    @Expose
    public String tipoEstadoConservacao;
    @SerializedName("Observacao")
    @Expose
    public String observacao;

}
