package br.ufrgs.cpd.inventario.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Theo on 29/09/17.
 */

public class Predios {
    @SerializedName("predios")
    @Expose
    public List<Predio> predios = null;
}
