package br.ufrgs.cpd.inventario.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.ufrgs.cpd.inventario.R
import kotlinx.android.synthetic.main.item_search.view.*

/**
 * Created by Theo on 28/09/2017.
 */
class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchVH>() {

    val mList = arrayListOf<String>()

    override fun getItemCount(): Int = mList.size


    override fun onBindViewHolder(holder: SearchVH, position: Int) { holder.bindView(mList[position])}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchVH =
            SearchVH(LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false))

    class SearchVH(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(str : String){
            itemView.text.text = str
        }
    }
}