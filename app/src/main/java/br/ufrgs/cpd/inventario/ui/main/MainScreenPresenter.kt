package br.ufrgs.cpd.inventario.ui.main

import android.content.Context
import br.ufrgs.cpd.inventario.models.Patrimonio
import br.ufrgs.cpd.inventario.network.ApiBuilder
import br.ufrgs.cpd.inventario.network.AppService
import br.ufrgs.ufrgsapi.network.pojo.ApiAnswer
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Theo on 04/10/2017.
 */
class MainScreenPresenter(val mContext: Context, val mView : MainScreenContract.View) : MainScreenContract.Presenter {

    override fun getItem(nrPatrimonio: String) {
        val retrofit = ApiBuilder.retrofitBuilder(mContext)

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
                    try {
                        val json = JSONObject(response.errorBody()!!.string())
                        val msg = json.getString("message")
                        if (msg.contentEquals("ntr")){
                            mView.showNtrMessage("Bem não tem registro (NTR)", nrPatrimonio)

                        } else {
                            mView.showMessage(response.message())
                        }

                    } catch (e : Exception){
                        mView.showMessage(response.message())
                    }
                }
            }

            override fun onFailure(call: Call<ApiAnswer<Patrimonio>>, t: Throwable) {
                mView.showMessage(t.message.toString())
            }
        })
    }

}
