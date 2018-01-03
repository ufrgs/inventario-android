package br.ufrgs.cpd.inventario.ui.pendencias_screen

import br.ufrgs.cpd.inventario.models.Pendencia

/**
 * Created by Theo on 06/10/17.
 */
interface PendenciasContract {
    interface View {
        fun showMessage(string: String)
        fun showPendencias(pendencias : List<Pendencia>)
        fun onSavePendenciaComplete()
    }

    interface Presenter{
        fun getPendencias()
        fun savePendencias(pendencias : List<Pendencia>, nrSeqColeta : String)
    }
}