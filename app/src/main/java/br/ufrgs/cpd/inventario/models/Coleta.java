package br.ufrgs.cpd.inventario.models;

import br.ufrgs.ufrgsapi.token.UfrgsTokenManager;

/**
 * Created by Theo on 04/10/2017.
 */

public class Coleta {

    public Coleta() {}

    public Coleta(Patrimonio patrimonio, String codPredio, String codEspacoFisico, String codOrgao) {
        NrPatrimonio = patrimonio.nrPatrimonio;
        Nome = patrimonio.nome;
        Marca = patrimonio.marca;
        Modelo = patrimonio.modelo;
        Serie = patrimonio.serie;
        Caracteristicas = patrimonio.caracteristicas;
        TipoSituacao = patrimonio.tipoSituacao;
        TipoEstadoConservacao = patrimonio.tipoEstadoConservacao;
        ObsSituacao = patrimonio.obsSituacao;
        CodPredio = codPredio;
        CodEspacoFisico = codEspacoFisico;
        CodPessoaCoResponsavel = patrimonio.codPessoaCoResponsavel;
        Foto = null;
        CodPessoaColeta = patrimonio.codPessoaResponsavel;
        CodOrgao = codOrgao;
    }
    public String NrPatrimonio;
    public String Nome;
    public String Marca;
    public String Modelo;
    public String Serie;
    public String Caracteristicas;
    public String TipoSituacao;
    public String TipoEstadoConservacao;
    public String ObsSituacao;
    public String CodPredio;
    public String CodEspacoFisico;
    public String CodPessoaCoResponsavel;
    public String Foto;
    public String CodPessoaColeta;
    public String CodOrgao;
}
