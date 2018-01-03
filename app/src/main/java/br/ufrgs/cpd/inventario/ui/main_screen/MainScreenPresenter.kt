package br.ufrgs.cpd.inventario.ui.main_screen

import android.content.Context
import android.util.Log
import br.ufrgs.cpd.inventario.models.Patrimonio
import br.ufrgs.cpd.inventario.network.AppService
import br.ufrgs.ufrgsapi.network.pojo.ApiAnswer
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Theo on 04/10/2017.
 */
class MainScreenPresenter(val mContext: Context, val mView : MainScreenContract.View) : MainScreenContract.Presenter {

    override fun getItem(nrPatrimonio: String) {
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
        val serviceCall = service.getPatrimonio(mTokenHeader, nrPatrimonio)

        serviceCall.enqueue(object : Callback<ApiAnswer<Patrimonio>> {
            override fun onResponse(call: Call<ApiAnswer<Patrimonio>>, response: Response<ApiAnswer<Patrimonio>>) {
                if (response.isSuccessful) {
                    try {
                        response.body()?.let {
                            mView.onItemReady(it.data)
                        }
                    } catch (e : Exception) {e.printStackTrace()}
                } else {
                    mView.showMessage(response.message())
                }
            }

            override fun onFailure(call: Call<ApiAnswer<Patrimonio>>, t: Throwable) {
                mView.showMessage(t.message.toString())
            }
        })
    }

}