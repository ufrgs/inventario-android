package br.ufrgs.cpd.inventario.ui.edit

import android.content.Context
import android.preference.PreferenceManager
import br.ufrgs.cpd.inventario.models.Coleta
import br.ufrgs.cpd.inventario.models.EstadosConservacao
import br.ufrgs.cpd.inventario.models.EstadosList
import br.ufrgs.cpd.inventario.models.ListPessoas
import br.ufrgs.cpd.inventario.network.ApiBuilder
import br.ufrgs.cpd.inventario.network.AppService
import br.ufrgs.ufrgsapi.network.pojo.ApiAnswer
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager
import br.ufrgs.ufrgsapi.user_data.UfrgsUser
import br.ufrgs.ufrgsapi.user_data.UfrgsUserDataManager
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Theo on 04/10/2017.
 */
class EditPresenter(private val mContext: Context, private val mView : EditContract.View) : EditContract.Presenter {


    override fun getResponsavel() {
        UfrgsUserDataManager(mContext).getData(object : UfrgsUserDataManager.OnDataCallback {
            override fun onDataReady(user: UfrgsUser?) {
                if (user != null) mView.onResponsavelReady(user.nomePessoa)
                else onError("Erro ao buscar responsável")
            }

            override fun onError(error: String?) {
                error?.let { mView.showMessage(it) }
            }

        })
    }

    private fun savePendenciaNtr(nrSeqColeta: String) {
        val retrofit = ApiBuilder.retrofitBuilder(mContext)

        val service = retrofit.create(AppService::class.java)
        val mTokenHeader = "Bearer " + UfrgsTokenManager().getToken(mContext).accessToken

        val serviceCall = service.savePendencias(mTokenHeader, nrSeqColeta, "2")

        serviceCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!response.isSuccessful) {
                    mView.showMessage(response.message())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                mView.showMessage(t.message.toString())
            }
        })
    }

    override fun getColetaStatus(patrimonio: String) {
        val retrofit = ApiBuilder.retrofitBuilder(mContext)

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

    override fun getCorresponsavel(identificador: String) {
        val retrofit = ApiBuilder.retrofitBuilder(mContext)

        val service = retrofit.create(AppService::class.java)
        val mTokenHeader = "Bearer " + UfrgsTokenManager().getToken(mContext).accessToken

        val serviceCall = service.getCorresponsavel(mTokenHeader, identificador)

        serviceCall.enqueue(object : Callback<ApiAnswer<ListPessoas>> {
            override fun onResponse(call: Call<ApiAnswer<ListPessoas>>, response: Response<ApiAnswer<ListPessoas>>) {
                mView.hideProgressDialog()
                if (response.isSuccessful) {
                    try {
                        if(response.isSuccessful) {
                            if(response.body()!!.data.pessoasList.isNotEmpty()){
                                response.body()?.let {
                                    mView.onGetCoResponsavel(it.data.pessoasList)
                                }
                            }
                            else{
                                mView.hideProgressDialog()
                                mView.showMessage("Nome ou cartão inválido")
                            }
                        } else {
                            mView.showMessage("Cartão inválido")
                        }
                    } catch (e : Exception) {e.printStackTrace()}
                } else {
                    mView.showMessage("Cartão inválido")
                }
            }

            override fun onFailure(call: Call<ApiAnswer<ListPessoas>>, t: Throwable) {
                mView.showMessage("Cartão inválido")
            }
        })
    }


    override fun getEstados() {
        val retrofit = ApiBuilder.retrofitBuilder(mContext)

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

    override fun saveColeta(coleta: Coleta, isNtr: Boolean, shouldFinish : Boolean) {
        val retrofit = ApiBuilder.retrofitBuilder(mContext)

        val service = retrofit.create(AppService::class.java)
        val mTokenHeader = "Bearer " + UfrgsTokenManager().getToken(mContext).accessToken

        val sdf = SimpleDateFormat("yyyy-MM-dd kk:mm:ss")
        val data = sdf.format(Date())

        val prefs = PreferenceManager.getDefaultSharedPreferences(mContext)
        val coletor = prefs.getString("cartao", "")

        val serviceCall = service.saveColeta(mTokenHeader,
                coleta.NrPatrimonio,
                coleta.Nome,
                coleta.Marca,
                coleta.Modelo,
                coleta.Serie,
                coleta.Caracteristicas,
                if (coleta.TipoSituacao.isNullOrBlank()) "15" else coleta.TipoSituacao,
                coleta.TipoEstadoConservacao,
                coleta.ObsSituacao,
                if (coleta.CodPredio.isNullOrEmpty()) mView.codpredio?.replace(".", "") else coleta.CodPredio?.replace(".", ""),
                if (coleta.CodEspacoFisico.isNullOrEmpty()) mView.codespaco?.replace(".", "") else coleta.CodEspacoFisico?.replace(".", ""),
                coleta.CodPessoaCoResponsavel,
                coletor,
                if (coleta.CodOrgao.isNullOrEmpty()) mView.codorgao?.replace(".", "") else coleta.CodOrgao?.replace(".", ""),
                data,
                "S",
                if (coleta.IndicadorOcioso) "S" else "N")

        serviceCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        if(response.isSuccessful) {
                            val json = JSONObject(response.body()?.string())
                            val nrColeta = json.getJSONObject("data").get("NrSeqColeta") as String
                            if (isNtr) savePendenciaNtr(nrColeta)

                            mView.onSavedComplete(nrColeta, shouldFinish)
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

    override fun getDiretor(codorgao: String) {
        val retrofit = ApiBuilder.retrofitBuilder(mContext)

        val service = retrofit.create(AppService::class.java)
        val mTokenHeader = "Bearer " + UfrgsTokenManager().getToken(mContext).accessToken

        val serviceCall = service.getDiretor(mTokenHeader, codorgao)

        serviceCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        if(response.isSuccessful) {
                            val json = JSONObject(response.body()?.string())
                            val diretor = json.getJSONObject("data").get("NomePessoa") as String
                            if(diretor != null) mView.onResponsavelReady(diretor)
                        } else {
                            mView.showMessage(response.message())
                        }
                    } catch (e : Exception) {e.printStackTrace()}
                } else {
                    mView.showMessage(response.message())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })
    }


}