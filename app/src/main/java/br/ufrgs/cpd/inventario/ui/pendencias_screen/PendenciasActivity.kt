package br.ufrgs.cpd.inventario.ui.pendencias_screen

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatCheckBox
import br.ufrgs.cpd.inventario.R
import br.ufrgs.cpd.inventario.models.Pendencia
import br.ufrgs.cpd.inventario.utils.LayoutUtils
import kotlinx.android.synthetic.main.activity_pendencias.*
import org.jetbrains.anko.toast
import br.ufrgs.cpd.inventario.R.id.linearLayout
import android.widget.CheckBox
import org.jetbrains.anko.sdk25.coroutines.onClick


/**
 * Created by Theo on 06/10/17.
 */
class PendenciasActivity : AppCompatActivity(), PendenciasContract.View {


    val listPendencias = arrayListOf<Pendencia>()
    lateinit var progressDialog : ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendencias)
        LayoutUtils.setupToolbar(this, toolbar, "Pendências", true)
        LayoutUtils.setStatusBarColor(this, "#6791ce")
        progressDialog = ProgressDialog(this)
        val nrSeqColeta = intent.getStringExtra("nrSeqColeta")

        progressDialog.setMessage("Carregando...")
        progressDialog.show()
        val presenter = PendenciasPresenter(this, this)
        presenter.getPendencias()

        btn_save.onClick {
            presenter.savePendencias(listPendencias, nrSeqColeta)
        }

    }

    override fun onSavePendenciaComplete() {
        toast("Salvo com sucesso")
        finish()
    }

    override fun showMessage(string: String) {
        if(progressDialog.isShowing)
            progressDialog.dismiss()

        toast(string)
    }

    @SuppressLint("RestrictedApi")
    override fun showPendencias(pendencias : List<Pendencia>) {
        if(progressDialog.isShowing)
            progressDialog.dismiss()

        for (i in 0 until pendencias.size) {

            val checkBox = AppCompatCheckBox(this)
            checkBox.text = pendencias[i].descricaoPendencia
            checkBox.isChecked = false
            //checkBox.setSupportButtonTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary))
            checkBox.onClick {
                if(checkBox.isChecked)
                    listPendencias.add(pendencias[i])
                else
                    listPendencias.remove(pendencias[i])
            }

            list.addView(checkBox)
        }
    }
}