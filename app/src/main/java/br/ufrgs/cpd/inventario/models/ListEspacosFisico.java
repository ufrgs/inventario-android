package br.ufrgs.cpd.inventario.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Theo on 03/10/2017.
 */

public class ListEspacosFisico {
    @SerializedName("espacos-fisicos")
    @Expose
    public List<EspacosFisico> espacosFisicos = null;
}
