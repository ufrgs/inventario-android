package br.ufrgs.cpd.inventario.ui.pendencias_screen

import android.content.Context
import br.ufrgs.cpd.inventario.models.ListPendencias
import br.ufrgs.cpd.inventario.models.Pendencia
import br.ufrgs.cpd.inventario.network.AppService
import br.ufrgs.ufrgsapi.network.pojo.ApiAnswer
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Theo on 06/10/17.
 */
class PendenciasPresenter(private val mContext: Context, private val mView : PendenciasContract.View) : PendenciasContract.Presenter {

    override fun savePendencias(pendencias: List<Pendencia>, nrSeqColeta : String) {
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

        if(pendencias.isEmpty())
            mView.onSavePendenciaComplete()

        for (i in 0 until pendencias.size){
            val serviceCall = service.savePendencias(mTokenHeader, nrSeqColeta, pendencias[i].tipoPendencia)

            serviceCall.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        try {
                            if(response.isSuccessful){
                                if(i == pendencias.lastIndex){
                                    mView.onSavePendenciaComplete()
                                }
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

    override fun getPendencias() {
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

        val serviceCall = service.getPendencias(mTokenHeader)

        serviceCall.enqueue(object : Callback<ApiAnswer<ListPendencias>> {
            override fun onResponse(call: Call<ApiAnswer<ListPendencias>>, response: Response<ApiAnswer<ListPendencias>>) {
                if (response.isSuccessful) {
                    try {
                        if(response.isSuccessful){
                            mView.showPendencias(response.body()?.data!!.pendenciasList)
                        } else{
                            mView.showMessage(response.message())
                        }
                    } catch (e : Exception) {e.printStackTrace()}
                } else {
                    mView.showMessage(response.message())
                }
            }

            override fun onFailure(call: Call<ApiAnswer<ListPendencias>>, t: Throwable) {
                mView.showMessage(t.message.toString())
            }
        })
    }
}