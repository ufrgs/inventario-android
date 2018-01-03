package br.ufrgs.cpd.inventario.ui.edit_ecreen

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import br.ufrgs.cpd.inventario.models.*
import br.ufrgs.cpd.inventario.network.AppService
import br.ufrgs.ufrgsapi.network.pojo.ApiAnswer
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager
import com.google.gson.JsonObject
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONObject
import org.xml.sax.Parser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Theo on 04/10/2017.
 */
class EditPresenter(private val mContext: Context, private val mView : EditContract.View) : EditContract.Presenter {

    override fun getColetaStatus(patrimonio: String) {
        val cache = Cache(mContext.applicationContext.cacheDir, 10 * 1024 * 1024)

        val okHttpClient = OkHttpClient.Builder()
                .cache(cache)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.ufrgs.br/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(AppService::class.java)
        val mTokenHeader = "Bearer " + UfrgsTokenManager().getToken(mContext).accessToken

        val serviceCall = service.getColeta(mTokenHeader, patrimonio)

        serviceCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    if(response.code() == 200)
                        mView.showMessage("Esta coleta já foi realizada")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })
    }

    override fun getCorresponsavel(cartao: String) {
        val cache = Cache(mContext.applicationContext.cacheDir, 10 * 1024 * 1024)

        val okHttpClient = OkHttpClient.Builder()
                .cache(cache)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.ufrgs.br/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(AppService::class.java)
        val mTokenHeader = "Bearer " + UfrgsTokenManager().getToken(mContext).accessToken

        val serviceCall = service.getCorresponsavel(mTokenHeader, cartao)

        serviceCall.enqueue(object : Callback<ApiAnswer<Pessoa>> {
            override fun onResponse(call: Call<ApiAnswer<Pessoa>>, response: Response<ApiAnswer<Pessoa>>) {
                if (response.isSuccessful) {
                    try {
                        if(response.isSuccessful) {
                            mView.onGetCoResponsavel(response.body()!!.data)
                        } else {
                            mView.showMessage("Cartão inválido")
                        }
                    } catch (e : Exception) {e.printStackTrace()}
                } else {
                    mView.showMessage("Cartão inválido")
                }
            }

            override fun onFailure(call: Call<ApiAnswer<Pessoa>>, t: Throwable) {
                mView.showMessage("Cartão inválido")
            }
        })
    }

    override fun getEstados() {
        val cache = Cache(mContext.applicationContext.cacheDir, 10 * 1024 * 1024)

        val okHttpClient = OkHttpClient.Builder()
                .cache(cache)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.ufrgs.br/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(AppService::class.java)
        val mTokenHeader = "Bearer " + UfrgsTokenManager().getToken(mContext).accessToken

        val serviceCall = service.getEstadosConservacao(mTokenHeader)

        serviceCall.enqueue(object : Callback<ApiAnswer<EstadosList>> {
            override fun onResponse(call: Call<ApiAnswer<EstadosList>>, response: Response<ApiAnswer<EstadosList>>) {
                if (response.isSuccessful) {
                    try {
                        if(response.isSuccessful) {
                            val list = response.body()!!.data.estadosConservacao
                            val vazio = EstadosConservacao()
                            vazio.descricaoEstadoConservacao = "NULL"
                            vazio.tipoEstadoConservacao = null
                            list.add(0, vazio)

                            mView.onGetEstados(list)
                        } else {
                            mView.showMessage(response.message())
                        }
                    } catch (e : Exception) {e.printStackTrace()}
                } else {
                    mView.showMessage(response.message())
                }
            }

            override fun onFailure(call: Call<ApiAnswer<EstadosList>>, t: Throwable) {
                mView.showMessage(t.message.toString())
            }
        })
    }

    override fun saveColeta(coleta: Coleta, shouldFinish : Boolean) {
        val cache = Cache(mContext.applicationContext.cacheDir, 10 * 1024 * 1024)

        val okHttpClient = OkHttpClient.Builder()
                .cache(cache)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.ufrgs.br/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create(AppService::class.java)
        val mTokenHeader = "Bearer " + UfrgsTokenManager().getToken(mContext).accessToken

        val sdf = SimpleDateFormat("yyyy-MM-dd kk:mm:ss")
        val data = sdf.format(Date())

        val prefs = PreferenceManager.getDefaultSharedPreferences(mContext)
        val coletor = prefs.getString("cartao", "")

        val serviceCall = service.saveColeta(mTokenHeader, coleta.NrPatrimonio, coleta.Nome, coleta.Marca,
                coleta.Modelo, coleta.Serie, coleta.Caracteristicas,
                coleta.TipoSituacao, coleta.TipoEstadoConservacao, coleta.ObsSituacao, coleta.CodPredio.replace(".", ""),
                coleta.CodEspacoFisico, coleta.CodPessoaCoResponsavel, coletor, coleta.CodOrgao.replace(".", ""), data, "S")

        serviceCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        if(response.isSuccessful) {
                            var json = JSONObject(response.body()?.string())
                            mView.onSavedComplete(json.getJSONObject("data").get("NrSeqColeta") as String, shouldFinish)
                        } else {
                            mView.showMessage(response.message())
                        }
                    } catch (e : Exception) {e.printStackTrace()}
                } else {
                    mView.showMessage(response.message())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                mView.showMessage(t.message.toString())
            }
        })
    }
}