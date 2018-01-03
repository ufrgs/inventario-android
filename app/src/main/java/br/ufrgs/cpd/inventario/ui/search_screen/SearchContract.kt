package br.ufrgs.cpd.inventario.ui.search_screen

import br.ufrgs.cpd.inventario.models.EspacosFisico
import br.ufrgs.cpd.inventario.models.Orgao
import br.ufrgs.cpd.inventario.models.Predio

/**
 * Created by Theo on 29/09/17.
 */
interface SearchContract {
    interface View {
        fun showPredios(list : List<Predio>)
        fun showOrgaos(list : List<Orgao>)
        fun showEspacosFisico(list : List<EspacosFisico>)
        fun showMessage(string: String)
    }

    interface Presenter {
        fun getEspacosFisicos(predio : String)
        fun getOrgaos()
    }
}