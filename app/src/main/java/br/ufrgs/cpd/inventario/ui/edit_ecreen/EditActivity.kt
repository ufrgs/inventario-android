package br.ufrgs.cpd.inventario.ui.edit_ecreen

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import br.ufrgs.cpd.inventario.R
import br.ufrgs.cpd.inventario.models.Coleta
import br.ufrgs.cpd.inventario.models.EstadosConservacao
import br.ufrgs.cpd.inventario.models.Patrimonio
import br.ufrgs.cpd.inventario.models.Pessoa
import br.ufrgs.cpd.inventario.ui.pendencias_screen.PendenciasActivity
import br.ufrgs.cpd.inventario.utils.LayoutUtils
import kotlinx.android.synthetic.main.activity_edit.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onItemSelectedListener


/**
 * Created by Theo on 28/09/2017.
 */
class EditActivity : AppCompatActivity(), EditContract.View {

    private lateinit var estados : List<EstadosConservacao>
    private lateinit var progressDialog : ProgressDialog
    private lateinit var patrimonio : Patrimonio
    private lateinit var coleta : Coleta

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        LayoutUtils.setupToolbar(this, toolbar, "Cadastrar coleta", true)
        LayoutUtils.setStatusBarColor(this, "#6791ce")

        progressDialog = ProgressDialog(this)

        patrimonio = intent.getSerializableExtra("patrimonio") as Patrimonio
        EditPresenter(this, this).getColetaStatus(patrimonio.nrPatrimonio)
        val codorgao = intent.getStringExtra("orgao")
        val codpredio = intent.getStringExtra("predio")
        val codespaco = intent.getStringExtra("espacofisico")
        coleta = Coleta(patrimonio, codpredio, codespaco, codorgao)

        nrpatrimonio.setText(patrimonio.nrPatrimonio)
        nome.setText(patrimonio.nome)
        marca.setText(patrimonio.marca)
        modelo.setText(patrimonio.modelo)
        serie.setText(patrimonio.serie)
        caracteristicas.setText(patrimonio.caracteristicas)
        responsavel.setText(patrimonio.nomeResponsavel)
        desc_situacao.setText(patrimonio.descricaoSituacao)
        coresponsavel.setText(patrimonio.nomeCoResponsavel)
        //desc_estado_cons.setText(patrimonio.descricaoEstadoConservacao)
        obs_situacao.setText(patrimonio.obsSituacao)

        btn_save.setOnClickListener {
            progressDialog.setMessage("Carregando...")
            progressDialog.show()


            coleta.Nome = nome.text.toString()
            coleta.Marca = marca.text.toString()
            coleta.Modelo = modelo.text.toString()
            coleta.Caracteristicas = caracteristicas.text.toString()
            coleta.ObsSituacao = obs_situacao.text.toString()

            EditPresenter(this, this).saveColeta(coleta, false)
        }

        btn_save_finish.setOnClickListener {
            if((spinner.selectedItem as String).contentEquals("NULL")){
                toast("Você precisa selecionar um estado de conservação")
            } else {
                progressDialog.setMessage("Carregando...")
                progressDialog.show()


                coleta.Nome = nome.text.toString()
                coleta.Marca = marca.text.toString()
                coleta.Modelo = modelo.text.toString()
                coleta.Caracteristicas = caracteristicas.text.toString()
                coleta.ObsSituacao = obs_situacao.text.toString()

                EditPresenter(this, this).saveColeta(coleta, true)
            }
        }

        coresponsavel.setOnClickListener {
            showDialogCoResponsavel()
           // EditPresenter(this, this).getCorresponsavel("00173230")
        }

        remove_co.onClick {
            alert("Deseja remover o co-responsável?") {
                yesButton {
                    coresponsavel.setText("")
                    patrimonio.nomeCoResponsavel = ""
                    patrimonio.codPessoaCoResponsavel = ""
                    coleta.CodPessoaCoResponsavel = ""
                }
                noButton {}
            }.show()
        }


        //---------


        EditPresenter(this, this).getEstados()

    }

    override fun onGetCoResponsavel(pessoa: Pessoa) {
        if(progressDialog.isShowing)
            progressDialog.dismiss()

        patrimonio.nomeCoResponsavel = pessoa.NomePessoa
        patrimonio.codPessoaCoResponsavel = pessoa.CodPessoa
        coleta.CodPessoaCoResponsavel = pessoa.CodPessoa
        coresponsavel.setText(patrimonio.nomeCoResponsavel)
    }


    private fun showDialogCoResponsavel() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_patrimonio, null)
        dialogBuilder.setView(dialogView)

        val et = dialogView.findViewById<EditText>(R.id.edit1)

        dialogBuilder.setMessage("Digite o cartão do CoResponsável")
        dialogBuilder.setPositiveButton("Ok", { dialog, whichButton ->
            progressDialog.setMessage("Carregando...")
            progressDialog.show()
            EditPresenter(this, this).getCorresponsavel(et.text.toString())
        })

        dialogBuilder.setNegativeButton("Cancelar", { dialog, whichButton ->
            dialog.dismiss()
        })
        val b = dialogBuilder.create()
        b.show()
    }

    override fun showMessage(string: String) {
        if(progressDialog.isShowing)
            progressDialog.dismiss()

        toast(string)
    }

    override fun onSavedComplete(nrSeqColeta : String, shouldFinish : Boolean) {
        if(progressDialog.isShowing)
            progressDialog.dismiss()
        if(!shouldFinish) {
            startActivity<PendenciasActivity>("nrSeqColeta" to nrSeqColeta)
            finish()
        } else {
            toast("Salvo com sucesso")
            finish()
        }
    }

    override fun onGetEstados(estados: List<EstadosConservacao>) {
        this.estados = estados

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayListOf<String>())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val list = ArrayList<String>()

        estados.mapTo(list) { it.descricaoEstadoConservacao }
        adapter.addAll(list)
        adapter.notifyDataSetChanged()

        if(list.contains(patrimonio.descricaoEstadoConservacao)) {
            spinner.adapter = adapter
            spinner.setSelection(list.indexOf(patrimonio.descricaoEstadoConservacao))
        } else{
            spinner.adapter = adapter
            spinner.setSelection(list.indexOf("NULL"))
        }
        spinner.onItemSelectedListener {


        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                estados
                        .filter { it.descricaoEstadoConservacao.contentEquals(list[position]) }
                        .forEach {
                            coleta.TipoEstadoConservacao = it.tipoEstadoConservacao
                        }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

                // sometimes you need nothing here
            }
        }

    }
}