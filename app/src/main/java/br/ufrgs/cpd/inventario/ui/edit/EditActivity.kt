package br.ufrgs.cpd.inventario.ui.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import br.ufrgs.cpd.inventario.R
import br.ufrgs.cpd.inventario.models.BemDescricao
import br.ufrgs.cpd.inventario.models.EstadosConservacao
import br.ufrgs.cpd.inventario.models.Patrimonio
import br.ufrgs.cpd.inventario.ui.search.SearchActivity
import br.ufrgs.cpd.inventario.utils.LayoutUtils
import kotlinx.android.synthetic.main.activity_edit.*
import org.jetbrains.anko.startActivity


/**
 * Created by Theo on 28/09/2017.
 */
class EditActivity : AppCompatActivity() {

    private val view = EditView(this)
    private val presenter = EditPresenter(this, view)

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        LayoutUtils.setupToolbar(this, toolbar, "Dados coleta", true)
        LayoutUtils.setStatusBarColor(this, "#6791ce")

        view.type = intent.getIntExtra(EXTRA_TYPE, -1)
        view.patrimonio = intent.getSerializableExtra(EXTRA_PATRIMONIO) as Patrimonio?
        view.codorgao = intent.getStringExtra(EXTRA_ORGAO)
        view.orgaoNome = intent.getStringExtra(EXTRA_ORGAO_NOME)
        view.codpredio = intent.getStringExtra(EXTRA_PREDIO)
        view.codespaco = intent.getStringExtra(EXTRA_ESPACO_FISICO)
        view.nrPatrimonio = intent.getStringExtra(EXTRA_NR_PATRIMONIO)



        if (view.type == TYPE_PLATE) {
            view.verifyOrgao()
            view.patrimonio?.let {presenter.getColetaStatus(it.nrPatrimonio) }
        }

        view.setupViews()
        view.setupButtons()
        view.initColeta()
        presenter.getEstados()

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == REQUEST_ESTADO){
                val selectedState = data?.getSerializableExtra("estado") as EstadosConservacao
                estado.setText(selectedState.descricaoEstadoConservacao)
                view.onTipoEstadoConservacaoChange(selectedState.tipoEstadoConservacao)
            } else if(requestCode == REQUEST_DESCRICAO) {
                val descricao = data?.getSerializableExtra(SearchActivity.OP_DESCRICOES) as BemDescricao
                view.onDescricaoChange(descricao)
            }
        }
    }


    companion object {
        private const val EXTRA_TYPE = "tipo"
        private const val EXTRA_PATRIMONIO = "patrimonio"
        private const val EXTRA_NR_PATRIMONIO = "nrPatrimonio"
        private const val EXTRA_ORGAO = "orgao"
        private const val EXTRA_ORGAO_NOME = "orgaoNome"
        private const val EXTRA_PREDIO = "predio"
        private const val EXTRA_ESPACO_FISICO = "espacofisico"

        const val TYPE_PLATE = 0
        const val TYPE_NO_PLATE = 1
        const val TYPE_NTR = 3
        const val REQUEST_ESTADO = 12
        const val REQUEST_DESCRICAO = 13
        const val EMPTY_TAG = "---------------------"

        //TYPE_PLATE
        fun startPlate(context: Context, type: Int, patrimonio: Patrimonio, orgao: String, orgaoNome: String, predio: String, espacofisico: String) {
            context.startActivity<EditActivity>(
                    EXTRA_TYPE to type,
                    EXTRA_PATRIMONIO to patrimonio,
                    EXTRA_ORGAO to orgao,
                    EXTRA_ORGAO_NOME to orgaoNome,
                    EXTRA_PREDIO to predio,
                    EXTRA_ESPACO_FISICO to espacofisico
            )
        }

        //TYPE_NO_PLATE
        fun startNoPlate(context: Context, type: Int, orgao: String, orgaoNome: String, predio: String, espacofisico: String) {
            context.startActivity<EditActivity>(
                    EXTRA_TYPE to type,
                    EXTRA_ORGAO to orgao,
                    EXTRA_ORGAO_NOME to orgaoNome,
                    EXTRA_PREDIO to predio,
                    EXTRA_ESPACO_FISICO to espacofisico)
        }

        //Start para TYPE_NTR
        fun startNtr(context: Context, nrPatrimonio: String,type: Int, orgao: String, orgaoNome: String, predio: String, espacofisico: String) {
            context.startActivity<EditActivity>(
                    EXTRA_TYPE to type,
                    EXTRA_NR_PATRIMONIO to nrPatrimonio,
                    EXTRA_ORGAO to orgao,
                    EXTRA_ORGAO_NOME to orgaoNome,
                    EXTRA_PREDIO to predio,
                    EXTRA_ESPACO_FISICO to espacofisico)
        }
    }
}