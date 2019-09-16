package br.ufrgs.cpd.inventario.ui.edit

import br.ufrgs.cpd.inventario.models.*

/**
 * Created by Theo on 04/10/2017.
 */
interface EditContract {
    interface View {
        fun setupViews()
        fun setupButtons()
        fun initColeta()
        fun onSavedComplete(nrSeqColeta : String, shouldFinish : Boolean)
        fun onGetEstados(estados : List<EstadosConservacao>)
        fun onGetCoResponsavel(pessoas : List<Pessoa>)
        fun verifyOrgao()
        fun showMessage(string: String)
        fun showProgressDialog()
        fun hideProgressDialog()
        fun onTipoEstadoConservacaoChange(tipoEstadosConservacao: String)
        fun onResponsavelReady(responsavelName: String)
        fun onDescricaoChange(descricao: BemDescricao)

        var type: Int?
        var patrimonio: Patrimonio?
        var nrPatrimonio: String?
        var codorgao: String?
        var orgaoNome: String?
        var codpredio: String?
        var codespaco: String?

    }

    interface Presenter {
        fun saveColeta(coleta: Coleta, isNtr: Boolean, shouldFinish : Boolean)
        fun getEstados()
        fun getCorresponsavel(identificador : String)
        fun getColetaStatus(patrimonio : String)
        fun getResponsavel()
        fun getDiretor(codorgao : String)
    }
}