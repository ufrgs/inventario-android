package br.ufrgs.cpd.inventario.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Theo on 17/11/2017.
 */

public class OrgaoAnswer implements Serializable {
    @SerializedName("orgaos")
    @Expose
    public List<Orgao> orgaos;
    @SerializedName("_meta")
    @Expose
    public Meta _meta;

}
