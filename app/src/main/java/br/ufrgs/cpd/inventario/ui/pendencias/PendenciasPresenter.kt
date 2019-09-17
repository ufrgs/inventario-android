package br.ufrgs.cpd.inventario.ui.pendencias

import android.content.Context
import android.util.Log
import br.ufrgs.cpd.inventario.models.ListPendencias
import br.ufrgs.cpd.inventario.models.Pendencia
import br.ufrgs.cpd.inventario.network.ApiBuilder
import br.ufrgs.cpd.inventario.network.AppService
import br.ufrgs.ufrgsapi.network.pojo.ApiAnswer
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Theo on 06/10/17.
 */
class PendenciasPresenter(private val mContext: Context, private val mView: PendenciasContract.View) : PendenciasContract.Presenter {

    override fun savePendencias(pendencias: List<Pendencia>, nrSeqColeta: String) {
        Log.d("pendendias", pendencias.toString())
        val retrofit = ApiBuilder.retrofitBuilder(mContext)

        val service = retrofit.create(AppService::class.java)
        val mTokenHeader = "Bearer " + UfrgsTokenManager().getToken(mContext).accessToken

        if (pendencias.isEmpty())
            mView.onSavePendenciaComplete()

        for (i in 0 until pendencias.size) {
            val serviceCall = service.savePendencias(mTokenHeader, nrSeqColeta, pendencias[i].tipoPendencia)

            serviceCall.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        try {
                            if (response.isSuccessful) {
                                if (i == pendencias.lastIndex) {
                                    mView.onSavePendenciaComplete()
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
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

        val retrofit = ApiBuilder.retrofitBuilder(mContext)

        val service = retrofit.create(AppService::class.java)
        val mTokenHeader = "Bearer " + UfrgsTokenManager().getToken(mContext).accessToken

        val serviceCall = service.getPendencias(mTokenHeader)

        serviceCall.enqueue(object : Callback<ApiAnswer<ListPendencias>> {
            override fun onResponse(call: Call<ApiAnswer<ListPendencias>>, response: Response<ApiAnswer<ListPendencias>>) {
                if (response.isSuccessful) {
                    try {
                        if (response.isSuccessful) {
                            mView.showPendencias(response.body()?.data!!.pendenciasList)
                        } else {
                            mView.showMessage(response.message())
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
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
