package br.ufrgs.cpd.inventario.ui.main

import br.ufrgs.cpd.inventario.models.Patrimonio

/**
 * Created by Theo on 04/10/2017.
 */
interface MainScreenContract {
    interface View {
        fun onItemReady(patrimonio : Patrimonio)
        fun showMessage(string: String)
        fun showNtrMessage(string: String, nrPatrimonio: String)
    }

    interface Presenter {
        fun getItem(nrPatrimonio : String)
    }
}