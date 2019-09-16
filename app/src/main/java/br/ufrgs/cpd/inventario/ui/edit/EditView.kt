package br.ufrgs.cpd.inventario.ui.edit

import android.app.Activity
import android.app.ProgressDialog
import androidx.appcompat.app.AlertDialog
import android.view.View
import android.widget.EditText
import br.ufrgs.cpd.inventario.R
import br.ufrgs.cpd.inventario.models.*
import br.ufrgs.cpd.inventario.ui.pendencias.PendenciasActivity
import br.ufrgs.cpd.inventario.ui.search.SearchActivity
import br.ufrgs.cpd.inventario.ui.tag.TagsActivity
import kotlinx.android.synthetic.main.activity_edit.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class EditView(val activity: Activity) : EditContract.View {

    override var type: Int? = null
    override var patrimonio: Patrimonio? = null
    override var codorgao: String? = null
    override var orgaoNome: String? = null
    override var codpredio: String? = null
    override var codespaco: String? = null
    override var nrPatrimonio: String? = null

    private var estados: List<EstadosConservacao> = arrayListOf()
    private var progressDialog: ProgressDialog? = null
    private val coleta = Coleta()
    private val presenter = EditPresenter(activity, this)

    override fun setupViews() {
        if (patrimonio != null) {
            activity.nrpatrimonio.setText(patrimonio!!.nrPatrimonio)
            activity.nome.setText(patrimonio!!.nome)
            activity.marca.setText(patrimonio!!.marca)
            activity.modelo.setText(patrimonio!!.modelo)
            activity.serie.setText(patrimonio!!.serie)
            activity.caracteristicas.setText(patrimonio!!.caracteristicas)
            activity.responsavel.setText(patrimonio!!.nomeResponsavel)
            activity.desc_situacao.setText(patrimonio!!.descricaoSituacao)
            activity.coresponsavel.setText(patrimonio!!.nomeCoResponsavel)
            activity.orgao.setText(patrimonio!!.nomeOrgaoResponsavel)
        }

        if (type == EditActivity.TYPE_NTR) {
            activity.nrpatrimonio.visibility = View.GONE
            activity.desc_situacaoao_lay.visibility = View.GONE
            orgaoNome?.let { activity.orgao.setText(it) }
            presenter.getDiretor(codorgao!!)
        } else if (type == EditActivity.TYPE_NO_PLATE) {
            activity.nrpatrimonio.visibility = View.GONE
            activity.desc_situacaoao_lay.visibility = View.GONE
            orgaoNome?.let { activity.orgao.setText(it) }
            presenter.getDiretor(codorgao!!)
        }

        activity.estado.setText(EditActivity.EMPTY_TAG)

    }

    override fun setupButtons() {
        activity.estado.onClick {
            if (!estados.isEmpty())
                activity.startActivityForResult<SelectStateActivity>(EditActivity.REQUEST_ESTADO, "estados" to estados)
        }

        activity.remove_co.onClick {
            activity.alert("Deseja remover o co-responsável?") {
                yesButton {
                    activity.coresponsavel.setText("")
                    patrimonio?.nomeCoResponsavel = ""
                    patrimonio?.codPessoaCoResponsavel = ""
                    coleta.CodPessoaCoResponsavel = ""
                }
                noButton {}
            }.show()
        }

        activity.coresponsavel.setOnClickListener {
            showDialogCoResponsavel()
        }

        activity.btn_save.onClick {
            coleta.Nome = activity.nome.text.toString()
            coleta.Marca = activity.marca.text.toString()
            coleta.Modelo = activity.modelo.text.toString()
            coleta.Caracteristicas = activity.caracteristicas.text.toString()
            coleta.IndicadorOcioso = activity.ocioso.isChecked

            if(nrPatrimonio != null) coleta.NrPatrimonio = nrPatrimonio

            if (validateFields()) {
                showProgressDialog()
                presenter.saveColeta(coleta, type == EditActivity.TYPE_NTR, true)
            }

        }

        //Descição personalizada
        activity.nome.onClick {
            SearchActivity.start(activity, EditActivity.REQUEST_DESCRICAO, SearchActivity.OP_DESCRICOES)
        }
    }

    override fun initColeta() {
        if (patrimonio != null) {
            coleta.initColeta(patrimonio, codpredio, codespaco, codorgao)
        }
    }

    override fun onDescricaoChange(descricao: BemDescricao) {
        activity.nome.setText(descricao.DescricaoPadronizada)
    }

    override fun onResponsavelReady(responsavelName: String) {
        activity.responsavel.setText(responsavelName)
    }

    override fun onSavedComplete(nrSeqColeta: String, shouldFinish: Boolean) {
        hideProgressDialog()
        when (type) {
            EditActivity.TYPE_NTR -> {
                activity.finish()
                TagsActivity.start(activity, TagColors.RED)
            }
            EditActivity.TYPE_PLATE -> {
                activity.alert("A descrição do bem está correta?") {
                    positiveButton("Sim") {
                        activity.finish()
                        TagsActivity.start(activity, TagColors.YELLOW)
                    }

                    negativeButton("Não") {
                        PendenciasActivity.start(activity, nrSeqColeta, type)
                        activity.finish()
                    }
                }.show()
            }
            else -> {
                PendenciasActivity.start(activity, nrSeqColeta, type)
                activity.finish()
            }
        }
    }

    override fun onGetEstados(estados: List<EstadosConservacao>) {
        val tempStates = arrayListOf<EstadosConservacao>()
        estados.forEach { if (!it.descricaoEstadoConservacao!!.contentEquals("NULL")) tempStates.add(it) }
        this.estados = tempStates
    }

    override fun onGetCoResponsavel(pessoas: List<Pessoa>) {
        hideProgressDialog()

        if (pessoas.size == 1) {
            val pessoa = pessoas[0]
            patrimonio?.nomeCoResponsavel = pessoa.NomePessoa
            patrimonio?.codPessoaCoResponsavel = pessoa.CodPessoa
            coleta.CodPessoaCoResponsavel = pessoa.CodPessoa
            activity.coresponsavel.setText(pessoa.NomePessoa)
        } else {
            val options = pessoas.map { it.NomePessoa }
            activity.selector("Corresponsavel", options) { _, i ->
                val pessoa = pessoas[i]
                patrimonio?.nomeCoResponsavel = pessoa.NomePessoa
                patrimonio?.codPessoaCoResponsavel = pessoa.CodPessoa
                coleta.CodPessoaCoResponsavel = pessoa.CodPessoa
                activity.coresponsavel.setText(pessoa.NomePessoa)
            }
        }
    }

    override fun showMessage(string: String) {
        activity.toast(string)
    }

    override fun showProgressDialog() {
        progressDialog = ProgressDialog(activity)
        progressDialog?.setMessage("Carregando...")
        progressDialog?.show()
    }

    override fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

    override fun verifyOrgao() {
        if (codorgao != patrimonio?.orgaoResponsavel) {
            activity.alert("O orgão selecionado não corresponde com o orgão do patrimônio") {
                yesButton { d -> d.dismiss() }
            }.show()
        }
    }

    override fun onTipoEstadoConservacaoChange(tipoEstadosConservacao: String) {
        coleta.TipoEstadoConservacao = tipoEstadosConservacao
    }

    private fun showDialogCoResponsavel() {
        val dialogBuilder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_patrimonio, null)
        dialogBuilder.setView(dialogView)

        val et = dialogView.findViewById<EditText>(R.id.edit1)

        dialogBuilder.setMessage("Digite o nome ou cartão do CoResponsável")
        dialogBuilder.setPositiveButton("Ok") { _, _ ->
            showProgressDialog()
            EditPresenter(activity, this).getCorresponsavel(et.text.toString())
        }

        dialogBuilder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        val b = dialogBuilder.create()
        b.show()
    }

    private fun validateFields(): Boolean {
        return when {
            activity.nome.text.isNullOrEmpty() -> {
                showMessage("Você deve atribuir um nome ao bem")
                false
            }
            activity.estado.text.contains(EditActivity.EMPTY_TAG) -> {
                showMessage("Você precisa selecionar um estado de conservação")
                false
            }
            activity.caracteristicas.text.isNullOrEmpty() -> {
                showMessage("Você precisa definir as características")
                false
            }
            else -> true
        }

    }
}