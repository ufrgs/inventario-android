package br.ufrgs.cpd.inventario.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Theo on 17/11/2017.
 */

public class Meta implements Serializable {
    @SerializedName("totalCount")
    @Expose
    public int totalCount;
    @SerializedName("pageCount")
    @Expose
    public int pageCount;
    @SerializedName("currentPage")
    @Expose
    public int currentPage;
    @SerializedName("perPage")
    @Expose
    public int perPage;
}
