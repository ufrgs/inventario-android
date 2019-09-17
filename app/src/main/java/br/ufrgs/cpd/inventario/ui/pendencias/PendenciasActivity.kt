package br.ufrgs.cpd.inventario.ui.pendencias

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import android.util.Log
import br.ufrgs.cpd.inventario.R
import br.ufrgs.cpd.inventario.models.Pendencia
import br.ufrgs.cpd.inventario.models.TagColors
import br.ufrgs.cpd.inventario.ui.edit.EditActivity
import br.ufrgs.cpd.inventario.ui.tag.TagsActivity
import br.ufrgs.cpd.inventario.utils.LayoutUtils
import kotlinx.android.synthetic.main.activity_pendencias.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


/**
 * Created by Theo on 06/10/17.
 */
class PendenciasActivity : AppCompatActivity(), PendenciasContract.View {


    private val type by lazy { intent.getIntExtra(TYPE, -1) }
    private val nrSeqColeta by lazy { intent.getStringExtra(NR_COLETA) }
    private val listPendencias = arrayListOf<Pendencia>()
    private val presenter = PendenciasPresenter(this, this)
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(type!= EditActivity.TYPE_PLATE) {
            setContentView(R.layout.activity_pendencias)
            LayoutUtils.setupToolbar(this, toolbar, "Pendências", false)
            LayoutUtils.setStatusBarColor(this, "#6791ce")

            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Carregando...")
            progressDialog.show()
            presenter.getPendencias()

            btn_save.onClick {
                if (listPendencias.isEmpty() && type == EditActivity.TYPE_NO_PLATE) {
                    toast("Você deve selecionar ao menos uma pendência.")
                } else {
                    presenter.savePendencias(listPendencias, nrSeqColeta)
                }
            }
        }
        else{
            progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Carregando...")
            progressDialog.show()
//            val listPendencias = arrayListOf<Pendencia>()
            val pendencia = Pendencia()
            pendencia.tipoPendencia = "8"
            pendencia.descricaoPendencia = "Alterar Descrição Padronizada"
            listPendencias.add(pendencia)
            showMessage("Foi adicionada uma pendência para alterar a descrição padronizada deste bem.")
            presenter.savePendencias(listPendencias, nrSeqColeta)
        }

    }

    override fun onSavePendenciaComplete() {
        toast("Salvo com sucesso")
        finish()
        showTagActivity()
    }

    override fun showMessage(string: String) {
        if (progressDialog.isShowing)
            progressDialog.dismiss()

        toast(string)
    }

    override fun showTagActivity() {
        val color = if (type == EditActivity.TYPE_NO_PLATE || listPendencias.isNotEmpty()) {
            TagColors.RED
        } else {
            TagColors.YELLOW
        }

        TagsActivity.start(this, color)
    }

    @SuppressLint("RestrictedApi")
    override fun showPendencias(pendencias: List<Pendencia>) {
        if (progressDialog.isShowing) progressDialog.dismiss()

        val filteredList = if (type == EditActivity.TYPE_NO_PLATE) {
            pendencias.filter {
                it.tipoPendencia == "9" ||
                        it.tipoPendencia == "5" ||
                        it.tipoPendencia == "1"
            }
        } else {
            pendencias
        }

        for (i in 0 until filteredList.size) {

            val checkBox = AppCompatCheckBox(this)
            checkBox.text = filteredList[i].descricaoPendencia
            checkBox.isChecked = false
            //checkBox.setSupportButtonTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary))
            checkBox.onClick {
                if (checkBox.isChecked) {
                    listPendencias.add(filteredList[i])
                    Log.d("debug", i.toString())
                }
                else
                    listPendencias.remove(filteredList[i])
            }

            list.addView(checkBox)
        }
    }

    companion object {

        private const val NR_COLETA = "nrSeqColeta"
        private const val TYPE = "type"

        fun start(context: Context, nrSequenciaColeta: String, type: Int?) {
            if (type != null){
                context.startActivity<PendenciasActivity>(NR_COLETA to nrSequenciaColeta, TYPE to type)
            } else {
                context.startActivity<PendenciasActivity>(NR_COLETA to nrSequenciaColeta)
            }
        }
    }
}