package br.ufrgs.cpd.inventario.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Theo on 06/10/17.
 */

public class ListPendencias {
    @SerializedName("items")
    @Expose
    public List<Pendencia> pendenciasList = null;
}
