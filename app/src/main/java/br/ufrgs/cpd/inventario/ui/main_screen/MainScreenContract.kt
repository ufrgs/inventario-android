package br.ufrgs.cpd.inventario.ui.main_screen

import br.ufrgs.cpd.inventario.models.Patrimonio

/**
 * Created by Theo on 04/10/2017.
 */
interface MainScreenContract {
    interface View {
        fun onItemReady(patrimonio : Patrimonio)
        fun showMessage(string: String)
    }

    interface Presenter {
        fun getItem(nrPatrimonio : String)
    }
}