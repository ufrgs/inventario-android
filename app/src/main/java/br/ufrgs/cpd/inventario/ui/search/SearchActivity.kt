package br.ufrgs.cpd.inventario.ui.search

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.ufrgs.cpd.inventario.R
import br.ufrgs.cpd.inventario.adapters.SearchAdapter
import br.ufrgs.cpd.inventario.models.BemDescricao
import br.ufrgs.cpd.inventario.models.EspacosFisico
import br.ufrgs.cpd.inventario.models.Orgao
import br.ufrgs.cpd.inventario.models.Predio
import br.ufrgs.cpd.inventario.utils.ItemClickSupport
import br.ufrgs.cpd.inventario.utils.LayoutUtils
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast


/**
 * Created by Theo on 28/09/2017.
 */
class SearchActivity : AppCompatActivity(), SearchContract.View, TextWatcher, ItemClickSupport.OnItemClickListener {

    private val presenter = SearchPresenter(this, this)
    private val operation by lazy { intent.getStringExtra("op") }
    private val mAdapter = SearchAdapter()
    private val listPredios = arrayListOf<Predio>()
    private val listOrgaos = arrayListOf<Orgao>()
    private val listEspacosFisico = arrayListOf<EspacosFisico>()
    private val listDescricoes = arrayListOf<BemDescricao>()
    private var listOriginal: ArrayList<String> = arrayListOf()
    private val progressDialog by lazy {  ProgressDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        LayoutUtils.setStatusBarColor(this, "#ececec")

        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = mAdapter
        ItemClickSupport.addTo(recyclerview).setOnItemClickListener(this)


        showLoader()
        when (operation) {
            "predio" -> {
                presenter.getPredios()
            }

            "orgao" -> {
                presenter.getOrgaos()
            }

            "espaco_fisico" -> {
                presenter.getEspacosFisicos(intent.getStringExtra("predio"))
            }

            OP_DESCRICOES -> {
                presenter.getDescricoes()
            }


        }

        searchview.addTextChangedListener(this)

        Handler().postDelayed({
            searchview.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }, (0.3 * 1000).toLong())


    }

    override fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View) {
        val itemClicked = mAdapter.mList[position]
        val pos = originalPos(itemClicked)

        val root = window.decorView.findViewById<View>(android.R.id.content)
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInputFromWindow(root.windowToken, 0, 0);

        when (operation) {
            "predio" -> {
                val resultIntent = Intent()
                resultIntent.putExtra("predio", listPredios[pos])
                setResult(RESULT_OK, resultIntent)
                finish()
            }

            "orgao" -> {
                val resultIntent = Intent()
                resultIntent.putExtra("orgao", listOrgaos[pos])
                setResult(RESULT_OK, resultIntent)
                finish()
            }

            "espaco_fisico" -> {
                val resultIntent = Intent()
                resultIntent.putExtra("espaco_fisico", listEspacosFisico[pos])
                setResult(RESULT_OK, resultIntent)
                finish()
            }

            OP_DESCRICOES -> {
                val resultIntent = Intent()
                resultIntent.putExtra(OP_DESCRICOES, listDescricoes[pos])
                setResult(RESULT_OK, resultIntent)
                finish()
            }

        }


    }

    override fun showMessage(string: String) {
        hideLoader()
        toast(string)
    }

    override fun showLoader() {
        progressDialog.setMessage("Carregando...")
        progressDialog.show()
    }

    override fun hideLoader() {
        progressDialog.dismiss()
    }

    override fun afterTextChanged(p0: Editable?) {}

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (p0.toString().isEmpty()) {
//            recyclerview.visibility = View.INVISIBLE
        } else {
            recyclerview.visibility = View.VISIBLE

            val tempList = arrayListOf<String>()

            val iterate = listOriginal.listIterator()
            while (iterate.hasNext()) {
                val item = iterate.next()
                if (item.toLowerCase().contains(p0.toString().toLowerCase())) tempList.add(item)
            }

            mAdapter.mList.clear()
            mAdapter.mList.addAll(tempList)
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun showOrgaos(list: List<Orgao>) {
        hideLoader()

        val strList = list.map { it.SiglaOrgao + " - " + it.NomeOrgao }

        mAdapter.mList.clear()
        mAdapter.mList.addAll(strList)
        mAdapter.notifyDataSetChanged()

        listOriginal = ArrayList(strList)
        listOrgaos.addAll(list)
    }

    override fun showPredios(list: List<Predio>) {
        hideLoader()

        val strList = list.map { "Nome: ${it.NomePredio}\nCódigo do prédio: ${it.CodPredio}\nCódigo semântico: ${it.CodSemantico}" }

        mAdapter.mList.clear()
        mAdapter.mList.addAll(strList)
        mAdapter.notifyDataSetChanged()

        listOriginal = ArrayList(strList)
        listPredios.addAll(list)
    }

    override fun showEspacosFisico(list: List<EspacosFisico>) {
        hideLoader()

        val strList = list.map { it.codSemantico.trim() + " - " + it.denominacaoEspacoFisico }

        mAdapter.mList.clear()
        mAdapter.mList.addAll(strList)
        mAdapter.notifyDataSetChanged()

        listOriginal = ArrayList(strList)
        listEspacosFisico.addAll(list)
    }

    private fun originalPos(value: String): Int {
        listOriginal.indices
                .asSequence()
                .filter { listOriginal[it].contentEquals(value) }
                .forEach { return it }

        return -1
    }

    override fun showDescricoes(list: List<BemDescricao>) {
        hideLoader()
        listDescricoes.addAll(list)
        val strList = list.map { it.DescricaoPadronizada }
        listOriginal = ArrayList(strList)
        mAdapter.mList.clear()
        mAdapter.mList.addAll(strList)
        mAdapter.notifyDataSetChanged()
    }

    companion object {

        const val OP_DESCRICOES = "descricoes"

        fun start(context: Context, requestCode: Int, requestoperation: String) {
            (context as AppCompatActivity).startActivityForResult<SearchActivity>(requestCode, "op" to requestoperation)
        }
    }
}