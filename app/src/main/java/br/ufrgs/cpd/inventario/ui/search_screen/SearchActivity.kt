package br.ufrgs.cpd.inventario.ui.search_screen

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import br.ufrgs.cpd.inventario.R
import br.ufrgs.cpd.inventario.adapters.SearchAdapter
import br.ufrgs.cpd.inventario.db.DatabaseAdapter
import br.ufrgs.cpd.inventario.models.EspacosFisico
import br.ufrgs.cpd.inventario.models.Orgao
import br.ufrgs.cpd.inventario.models.Predio
import br.ufrgs.cpd.inventario.utils.ItemClickSupport
import br.ufrgs.cpd.inventario.utils.LayoutUtils
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.toast


/**
 * Created by Theo on 28/09/2017.
 */
class SearchActivity : AppCompatActivity(), SearchContract.View, TextWatcher, ItemClickSupport.OnItemClickListener {



    private val mAdapter = SearchAdapter()
    private val listPredios = arrayListOf<Predio>()
    private val listOrgaos = arrayListOf<Orgao>()
    private val listEspacosFisico = arrayListOf<EspacosFisico>()
    lateinit var listOriginal : ArrayList<String>
    lateinit var operation : String
    lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        LayoutUtils.setStatusBarColor(this, "#ececec")

        progressDialog = ProgressDialog(this)

        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = mAdapter
        ItemClickSupport.addTo(recyclerview).setOnItemClickListener(this)

        operation = intent.getStringExtra("op")

        when(operation){
            "predio" -> {
                val predios = DatabaseAdapter(this).getAllPredios()
                showPredios(predios)
            }

            "orgao" -> {
                progressDialog.setMessage("Carregando...")
                progressDialog.show()
                SearchPresenter(this, this).getOrgaos()
            }

            "espaco_fisico" -> {
                progressDialog.setMessage("Carregando...")
                progressDialog.show()
                SearchPresenter(this, this).getEspacosFisicos(intent.getStringExtra("predio"))
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

        val root = getWindow().getDecorView().findViewById<View>(android.R.id.content)
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInputFromWindow(root.getWindowToken(), 0,0);

        when(operation){
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

        }


    }

    override fun showMessage(string: String) {
        if(progressDialog.isShowing)
            progressDialog.dismiss()

        toast(string)
    }

    override fun afterTextChanged(p0: Editable?) { }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if(p0.toString().isEmpty()){
            recyclerview.visibility = View.INVISIBLE
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
        if(progressDialog.isShowing)
            progressDialog.dismiss()

        val strList = arrayListOf<String>()

        repeat(list.size, {
            strList.add(list[it].SiglaOrgao + " - " + list[it].NomeOrgao)
        })

        mAdapter.mList.clear()
        mAdapter.mList.addAll(strList)
        mAdapter.notifyDataSetChanged()

        listOriginal = strList
        listOrgaos.addAll(list)
    }

    override fun showPredios(list: List<Predio>) {
        val strList = arrayListOf<String>()

        repeat(list.size, {
            strList.add(list[it].CodPredio + " - " + list[it].NomePredio)
        })

        mAdapter.mList.clear()
        mAdapter.mList.addAll(strList)
        mAdapter.notifyDataSetChanged()

        listOriginal = strList
        listPredios.addAll(list)
    }

    override fun showEspacosFisico(list: List<EspacosFisico>) {
        if(progressDialog.isShowing)
            progressDialog.dismiss()

        val strList = arrayListOf<String>()

        repeat(list.size, {
            strList.add(list[it].codSemantico.trim() + " - " + list[it].denominacaoEspacoFisico)
        })

        mAdapter.mList.clear()
        mAdapter.mList.addAll(strList)
        mAdapter.notifyDataSetChanged()

        listOriginal = strList
        listEspacosFisico.addAll(list)
    }

    private fun originalPos(value : String) : Int {
        listOriginal.indices
                .asSequence()
                .filter { listOriginal[it].contentEquals(value) }
                .forEach { return it }

        return -1
    }
}