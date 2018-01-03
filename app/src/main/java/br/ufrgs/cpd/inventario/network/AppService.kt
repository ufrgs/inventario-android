package br.ufrgs.cpd.inventario.network

import br.ufrgs.cpd.inventario.models.*
import br.ufrgs.ufrgsapi.network.pojo.ApiAnswer
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Theo on 29/09/17.
 */
interface AppService {

    @GET("v1/predios")
    fun getPredios(@Header("Authorization") auth : String) : Call<ApiAnswer<Predios>>

    @GET("v1/meus-locais")
    fun getOrgaos(@Header("Authorization") auth : String, @Query("page") page : Int) : Call<ApiAnswer<OrgaoAnswer>>

    @GET("v1/espacos-fisicos/{predio}")
    fun getEspacosFisicos(@Header("Authorization") auth : String, @Path("predio") predio : String): Call<ApiAnswer<ListEspacosFisico>>

    @GET("v1/inventario/bem/{nr}")
    fun getPatrimonio(@Header("Authorization") auth : String, @Path("nr") nrPatrimonio : String): Call<ApiAnswer<Patrimonio>>

    @FormUrlEncoded
    @POST("v1/inventario/coleta-inventario")
    fun saveColeta(@Header("Authorization") auth : String,
                   @Field("NrPatrimonio") NrPatrimonio : String?,
                   @Field("Nome") Nome : String?,
                   @Field("Marca") Marca : String?,
                   @Field("Modelo") Modelo : String?,
                   @Field("Serie") Serie : String?,
                   @Field("Caracteristicas") Caracteristicas : String?,
                   @Field("TipoSituacao") TipoSituacao : String?,
                   @Field("TipoEstadoConservacao") TipoEstadoConservacao : String?,
                   @Field("ObsSituacao") ObsSituacao : String?,
                   @Field("CodPredio") CodPredio : String?,
                   @Field("CodEspacoFisico") CodEspacoFisico : String?,
                   @Field("CodPessoaCoResponsavel") CodPessoaCoResponsavel : String?,
                   @Field("CodPessoaColeta") CodPessoaColeta : String?,
                   @Field("CodOrgao") CodOrgao : String?,
                   @Field("DataHoraColeta") DataHoraColeta : String?,
                   @Field("IndicadorAndroid") IndicadorAndroid : String?
    ) : Call<ResponseBody>

    @GET("v1/inventario/coleta/{patrimonio}")
    fun getColeta(@Header("Authorization") auth : String, @Path("patrimonio") patrimonio : String) : Call<ResponseBody>

    @GET("v1/inventario/pendencias")
    fun getPendencias(@Header("Authorization") auth : String): Call<ApiAnswer<ListPendencias>>

    @FormUrlEncoded
    @POST("v1/inventario/pendencia")
    fun savePendencias(@Header("Authorization") auth : String, @Field("NrSeqColeta") nrSeqColeta : String, @Field("TipoPendencia") tipoPendencia : String): Call<ResponseBody>

    @GET("v1/inventario/estados-conservacao")
    fun getEstadosConservacao(@Header("Authorization") auth : String): Call<ApiAnswer<EstadosList>>

    @GET("v1/pessoa/{cartao}")
    fun getCorresponsavel(@Header("Authorization") auth : String, @Path("cartao") cartao : String): Call<ApiAnswer<Pessoa>>


}