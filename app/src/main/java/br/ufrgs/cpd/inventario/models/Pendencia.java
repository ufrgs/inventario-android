package br.ufrgs.cpd.inventario.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Theo on 06/10/17.
 */

public class Pendencia implements Serializable {
    @SerializedName("TipoPendencia")
    @Expose
    public String tipoPendencia;
    @SerializedName("DescricaoPendencia")
    @Expose
    public String descricaoPendencia;
}
