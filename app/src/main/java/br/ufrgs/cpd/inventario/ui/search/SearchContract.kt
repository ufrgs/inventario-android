package br.ufrgs.cpd.inventario.ui.search

import br.ufrgs.cpd.inventario.models.BemDescricao
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
        fun showDescricoes(list: List<BemDescricao>)
        fun showLoader()
        fun hideLoader()
    }

    interface Presenter {
        fun getEspacosFisicos(predio : String)
        fun getOrgaos()
        fun getPredios()
        fun getDescricoes()
    }
}