package br.ufrgs.cpd.inventario.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufrgs.cpd.inventario.R
import br.ufrgs.cpd.inventario.models.EstadosConservacao
import kotlinx.android.synthetic.main.item_estado_conservacao.view.*

class SelectStateAdapter(val list: List<EstadosConservacao>) : RecyclerView.Adapter<SelectStateAdapter.SelectStateViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectStateViewHolder =
            SelectStateViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_estado_conservacao, parent, false))


    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SelectStateViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    class SelectStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(estado : EstadosConservacao){
            itemView.state.text = estado.descricaoEstadoConservacao
            itemView.description.text = estado.observacao
        }
    }
}