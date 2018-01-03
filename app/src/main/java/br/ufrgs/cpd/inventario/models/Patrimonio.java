package br.ufrgs.cpd.inventario.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Theo on 04/10/2017.
 */

public class Patrimonio implements Serializable {
    @SerializedName("NrPatrimonio")
    @Expose
    public String nrPatrimonio;
    @SerializedName("CodDescricao")
    @Expose
    public String codDescricao;
    @SerializedName("Nome")
    @Expose
    public String nome;
    @SerializedName("Marca")
    @Expose
    public String marca;
    @SerializedName("Modelo")
    @Expose
    public String modelo;
    @SerializedName("Serie")
    @Expose
    public String serie;
    @SerializedName("MarcaModeloSerie")
    @Expose
    public String marcaModeloSerie;
    @SerializedName("Caracteristicas")
    @Expose
    public String caracteristicas;
    @SerializedName("MatriculaResponsavel")
    @Expose
    public String matriculaResponsavel;
    @SerializedName("CodPessoaResponsavel")
    @Expose
    public String codPessoaResponsavel;
    @SerializedName("NomeResponsavel")
    @Expose
    public String nomeResponsavel;
    @SerializedName("TipoSituacao")
    @Expose
    public String tipoSituacao;
    @SerializedName("DescricaoSituacao")
    @Expose
    public String descricaoSituacao;
    @SerializedName("TipoEstadoConservacao")
    @Expose
    public String tipoEstadoConservacao;
    @SerializedName("DescricaoEstadoConservacao")
    @Expose
    public String descricaoEstadoConservacao;
    @SerializedName("IndicadorBaixado")
    @Expose
    public String indicadorBaixado;
    @SerializedName("ObsSituacao")
    @Expose
    public String obsSituacao;
    @SerializedName("NomeCoResponsavel")
    @Expose
    public String nomeCoResponsavel;
    @SerializedName("CodPessoaCoResponsavel")
    @Expose
    public String codPessoaCoResponsavel;
}
