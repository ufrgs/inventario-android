package br.ufrgs.cpd.inventario.ui.search

import android.content.Context
import br.ufrgs.cpd.inventario.models.ListEspacosFisico
import br.ufrgs.cpd.inventario.models.Orgao
import br.ufrgs.cpd.inventario.models.Predios
import br.ufrgs.cpd.inventario.network.ApiBuilder
import br.ufrgs.cpd.inventario.network.AppService
import br.ufrgs.cpd.inventario.usecase.LogoutUseCase
import br.ufrgs.ufrgsapi.network.pojo.ApiAnswer
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by Theo on 29/09/17.
 */
class SearchPresenter(private val mContext: Context, private val mView: SearchContract.View) : SearchContract.Presenter {
    private val logoutUseCase = LogoutUseCase(mContext)

    override fun getDescricoes() {

        val retrofit = ApiBuilder.retrofitBuilder(mContext)

        val service = retrofit.create(AppService::class.java)
        val mTokenHeader = "Bearer " + UfrgsTokenManager().getToken(mContext).accessToken

        val obs = service.getDescricaoPadrao(mTokenHeader)

        obs.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mView.showDescricoes(it.data.bem)
                }, {
                    it.message?.let {
                        it1 -> mView.showMessage(it1)
                    }
                })

    }

    override fun getPredios() {
        val retrofit = ApiBuilder.retrofitBuilder(mContext)

        val service = retrofit.create(AppService::class.java)
        val mTokenHeader = "Bearer " + UfrgsTokenManager().getToken(mContext).accessToken
        val serviceCall = service.getPredios(mTokenHeader)

        serviceCall.enqueue(object : Callback<ApiAnswer<Predios>> {
            override fun onFailure(call: Call<ApiAnswer<Predios>>?, t: Throwable?) {
                t?.message?.let { mView.showMessage(it) }
            }

            override fun onResponse(call: Call<ApiAnswer<Predios>>?, response: Response<ApiAnswer<Predios>>) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            mView.showPredios(it.data.predios)
                        }
                    }

                    401 -> {
                        logoutUseCase.logout()
                    }

                    else -> {
                        mView.showMessage(response.message())
                    }
                }
            }

        })


    }

    override fun getOrgaos() {
        val retrofit = ApiBuilder.retrofitBuilder(mContext)

        val service = retrofit.create(AppService::class.java)
        val mTokenHeader = "Bearer " + UfrgsTokenManager().getToken(mContext).accessToken
        val serviceCall = service.getOrgaos(mTokenHeader, 1)

        doAsync {
            val out = arrayListOf<Orgao>()
            val response = serviceCall.execute()

            when {
                response.code() == 200 -> {
                    val firstData = response.body()
                    out.addAll(firstData!!.data.orgaos)
                    val totalPages = firstData.data._meta.pageCount

                    if (totalPages > 1) {
                        for (i in 2..totalPages) {
                            val call = service.getOrgaos(mTokenHeader, i)
                            val data = call.execute().body()
                            out.addAll(data!!.data.orgaos)
                        }
                    }

                    uiThread {
                        mView.showOrgaos(out)
                    }
                }
                response.code() == 401 -> {
                    uiThread {
                        logoutUseCase.logout()
                    }
                }

                else -> {
                    uiThread {
                        mView.showMessage("Erro ao carregar informações")
                    }
                }
            }

        }

    }

    override fun getEspacosFisicos(predio: String) {
        val retrofit = ApiBuilder.retrofitBuilder(mContext)

        val service = retrofit.create(AppService::class.java)
        val mTokenHeader = "Bearer " + UfrgsTokenManager().getToken(mContext).accessToken
        val serviceCall = service.getEspacosFisicos(mTokenHeader, predio)

        serviceCall.enqueue(object : Callback<ApiAnswer<ListEspacosFisico>> {
            override fun onResponse(call: Call<ApiAnswer<ListEspacosFisico>>, response: Response<ApiAnswer<ListEspacosFisico>>) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            mView.showEspacosFisico(it.data.espacosFisicos)
                        }
                    }

                    401 -> {
                        logoutUseCase.logout()
                    }

                    else -> {
                        mView.showMessage(response.message())
                    }
                }
            }

            override fun onFailure(call: Call<ApiAnswer<ListEspacosFisico>>, t: Throwable) {
                mView.showMessage(t.message.toString())
            }
        })
    }
}
