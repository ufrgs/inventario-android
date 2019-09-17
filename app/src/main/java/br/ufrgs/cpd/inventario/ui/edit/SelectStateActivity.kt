package br.ufrgs.cpd.inventario.ui.edit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.ufrgs.cpd.inventario.R
import br.ufrgs.cpd.inventario.adapters.SelectStateAdapter
import br.ufrgs.cpd.inventario.models.EstadosConservacao
import br.ufrgs.cpd.inventario.utils.ItemClickSupport
import br.ufrgs.cpd.inventario.utils.LayoutUtils
import kotlinx.android.synthetic.main.activity_recycler.*

class SelectStateActivity : AppCompatActivity() {

    private val estadosConservacao: List<EstadosConservacao> by lazy { intent.getSerializableExtra("estados") as List<EstadosConservacao>}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        LayoutUtils.setupToolbar(this, toolbar, "Estado de conservação", true)

        val adapter = SelectStateAdapter(estadosConservacao)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter

        ItemClickSupport.addTo(recyclerview).setOnItemClickListener { _, position, _ ->
            val item = adapter.list[position]
            val data = Intent()
            data.putExtra("estado", item)
            setResult(RESULT_OK, data)
            finish()
        }


    }
}