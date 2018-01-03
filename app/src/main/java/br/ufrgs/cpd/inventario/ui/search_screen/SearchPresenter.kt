package br.ufrgs.cpd.inventario.ui.search_screen

import android.content.Context
import android.util.Log
import br.ufrgs.cpd.inventario.models.ListEspacosFisico
import br.ufrgs.cpd.inventario.models.Orgao
import br.ufrgs.cpd.inventario.models.OrgaoAnswer
import br.ufrgs.cpd.inventario.network.AppService
import br.ufrgs.cpd.inventario.models.Predios
import br.ufrgs.ufrgsapi.network.pojo.ApiAnswer
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by Theo on 29/09/17.
 */
class SearchPresenter(private val mContext : Context, private val mView : SearchContract.View) : SearchContract.Presenter {
    override fun getOrgaos() {
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
        val serviceCall = service.getOrgaos(mTokenHeader, 1)

//        val teste = serviceCall.execute().body()
//        Log.d("", teste.toString())
        doAsync {
            val out = arrayListOf<Orgao>()
            val firstData = serviceCall.execute().body()
            out.addAll(firstData!!.data.orgaos)
            val totalPages = firstData.data._meta.pageCount

            if (totalPages > 1) {
                for (i in 2..totalPages){
                    val call = service.getOrgaos(mTokenHeader, i)
                    val data = call.execute().body()
                    out.addAll(data!!.data.orgaos)
                }
            }

            uiThread {
                mView.showOrgaos(out)
            }
        }

    }

    override fun getEspacosFisicos(predio: String) {
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
        val serviceCall = service.getEspacosFisicos(mTokenHeader, predio)

        serviceCall.enqueue(object : Callback<ApiAnswer<ListEspacosFisico>> {
            override fun onResponse(call: Call<ApiAnswer<ListEspacosFisico>>, response: Response<ApiAnswer<ListEspacosFisico>>) {
                if (response.isSuccessful) {
                    try {
                        response.body()?.let {
                            mView.showEspacosFisico(it.data.espacosFisicos)
                        }
                    } catch (e : Exception) {e.printStackTrace()}
                } else {
                    mView.showMessage(response.message())
                }
            }

            override fun onFailure(call: Call<ApiAnswer<ListEspacosFisico>>, t: Throwable) {
                mView.showMessage(t.message.toString())
            }
        })
    }
}