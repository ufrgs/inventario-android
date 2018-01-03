package br.ufrgs.cpd.inventario.ui.edit_ecreen

import br.ufrgs.cpd.inventario.models.Coleta
import br.ufrgs.cpd.inventario.models.EstadosConservacao
import br.ufrgs.cpd.inventario.models.Pessoa

/**
 * Created by Theo on 04/10/2017.
 */
interface EditContract {
    interface View {
        fun onSavedComplete(nrSeqColeta : String, shouldFinish : Boolean)
        fun onGetEstados(estados : List<EstadosConservacao>)
        fun onGetCoResponsavel(pessoa : Pessoa)
        fun showMessage(string: String)
    }

    interface Presenter {
        fun saveColeta(coleta: Coleta, shouldFinish : Boolean)
        fun getEstados()
        fun getCorresponsavel(cartao : String)
        fun getColetaStatus(patrimonio : String)
    }
}