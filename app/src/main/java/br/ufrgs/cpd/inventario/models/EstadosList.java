package br.ufrgs.cpd.inventario.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Theo on 13/10/17.
 */

public class EstadosList {
    @SerializedName("espacos-conservacao")
    @Expose
    public List<EstadosConservacao> estadosConservacao = null;
}
