package br.ufrgs.cpd.inventario.ui.main_screen

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.Toast
import br.ufrgs.cpd.inventario.R
import br.ufrgs.cpd.inventario.models.EspacosFisico
import br.ufrgs.cpd.inventario.models.Orgao
import br.ufrgs.cpd.inventario.models.Patrimonio
import br.ufrgs.cpd.inventario.models.Predio
import br.ufrgs.cpd.inventario.ui.config_screen.ConfigActivity
import br.ufrgs.cpd.inventario.ui.edit_ecreen.EditActivity
import br.ufrgs.cpd.inventario.ui.search_screen.SearchActivity
import br.ufrgs.cpd.inventario.utils.LayoutUtils
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity(), MainScreenContract.View {

    lateinit var mSelectedOrgao : Orgao
    lateinit var mSelectedPredio : Predio
    lateinit var mSelectedEspacoFisico : EspacosFisico
    lateinit var progressDialog : ProgressDialog

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LayoutUtils.setStatusBarColor(this, "#6791ce")
        LayoutUtils.setNavbarTranslucent(this)
        progressDialog = ProgressDialog(this)

        btn1.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ic_search1.transitionName = "ic_search"
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity, ic_search1, "ic_search")
                intent.putExtra("op", "orgao")
                startActivityForResult(intent, REQUEST_CODE, options.toBundle())
            } else {
                startActivityForResult<SearchActivity>(REQUEST_CODE,"op" to "orgao")
            }

        }

        btn1.setOnLongClickListener {
            toast(btn1_txt.text.toString())
            return@setOnLongClickListener true
        }

        btn2.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ic_search2.transitionName = "ic_search"
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity, ic_search2, "ic_search")
                intent.putExtra("op", "predio")
                startActivityForResult(intent, REQUEST_CODE, options.toBundle())
            } else {
                startActivityForResult<SearchActivity>(REQUEST_CODE,"op" to "predio")
            }

        }

        btn2.setOnLongClickListener {
            toast(btn2_txt.text.toString())
            return@setOnLongClickListener true
        }

        btn3.setOnClickListener {
            try {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ic_search3.transitionName = "ic_search"
                    val intent = Intent(this@MainActivity, SearchActivity::class.java)
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity, ic_search3, "ic_search")
                    intent.putExtra("op", "espaco_fisico")
                    intent.putExtra("predio", mSelectedPredio.CodEspacoFisico)
                    startActivityForResult(intent, REQUEST_CODE, options.toBundle())
                } else {
                    val i = Intent(this, SearchActivity::class.java)
                    i.putExtra("op", "espaco_fisico")
                    i.putExtra("predio", mSelectedPredio.CodEspacoFisico)
                    startActivityForResult(i,REQUEST_CODE)
                }

            } catch (e : Exception){
                toast("Você precisa selecionar o orgão e prédio antes de selecionar o espaço físico")
            }
        }

        btn3.setOnLongClickListener {
            toast(btn3_txt.text.toString())
            return@setOnLongClickListener true
        }

        btn_barcode.setOnClickListener {

            try {
                val a = mSelectedOrgao.CodOrgao
                val b = mSelectedPredio.CodPredio
                val c = mSelectedEspacoFisico.codEspacoFisico

                val integrator = IntentIntegrator(this)
                integrator.setOrientationLocked(false)
                //integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
                integrator.setPrompt("Escaneie um código de barras")
                integrator.setCameraId(0)  // Use a specific camera of the device
                integrator.setBeepEnabled(true)
                integrator.setBarcodeImageEnabled(true)
                integrator.initiateScan()
            } catch (e : Exception){
                toast("Você precisa selecionar orgão, prédio e espaço físico")
            }

        }

        btn_digit.setOnClickListener{
            try {
                val a = mSelectedOrgao.CodOrgao
                val b = mSelectedPredio.CodPredio
                val c = mSelectedEspacoFisico.codEspacoFisico

                showChangeLangDialog()


            } catch (e : Exception){
                toast("Você precisa selecionar orgão, prédio e espaço físico")
            }
        }

        btn_settings.onClick {
            startActivity<ConfigActivity>()
        }

    }

    override fun showMessage(string: String) {
        if(progressDialog.isShowing)
            progressDialog.dismiss()

        toast(string)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            //QRCODE
            if (result.contents == null) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            } else {
                progressDialog.setMessage("Carregando...")
                progressDialog.show()
                MainScreenPresenter(this, this).getItem(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
            if(requestCode == REQUEST_CODE && data != null){
                //VEIO da tela de selecao
                when {
                    data.hasExtra("orgao") -> {
                        mSelectedOrgao = data.getSerializableExtra("orgao") as Orgao
                    }
                    data.hasExtra("predio") -> {
                        mSelectedPredio = data.getSerializableExtra("predio") as Predio
                    }
                    data.hasExtra("espaco_fisico") -> {
                        mSelectedEspacoFisico = data.getSerializableExtra("espaco_fisico") as EspacosFisico
                    }
                }

                updateButtonText()
            }
        }
    }

    override fun onItemReady(patrimonio : Patrimonio) {
        if(progressDialog.isShowing)
            progressDialog.dismiss()

        val i = Intent(this, EditActivity::class.java)
        i.putExtra("patrimonio", patrimonio)
        i.putExtra("orgao", mSelectedOrgao.CodOrgao)
        i.putExtra("predio", mSelectedPredio.CodPredio)
        i.putExtra("espacofisico", mSelectedEspacoFisico.codEspacoFisico)
        startActivity(i)
    }

    private fun updateButtonText(){
        try{ btn1_txt.text = mSelectedOrgao.NomeOrgao} catch (e : UninitializedPropertyAccessException) { btn1_txt.text = "Órgão" }
        try{ btn2_txt.text = mSelectedPredio.NomePredio } catch (e : UninitializedPropertyAccessException) { btn2_txt.text = "Prédio" }
        try{ btn3_txt.text = mSelectedEspacoFisico.denominacaoEspacoFisico } catch (e : UninitializedPropertyAccessException) { btn3_txt.text = "Espaço Físico" }
    }

    fun showChangeLangDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_patrimonio, null)
        dialogBuilder.setView(dialogView)

        val et = dialogView.findViewById<EditText>(R.id.edit1)

        dialogBuilder.setMessage("Entre com o número de patrimônio")
        dialogBuilder.setPositiveButton("Ok", { dialog, whichButton ->
            progressDialog.setMessage("Carregando...")
            progressDialog.show()
            MainScreenPresenter(this, this).getItem(et.text.toString())
        })

        dialogBuilder.setNegativeButton("Cancelar", { dialog, whichButton ->
            dialog.dismiss()
        })
        val b = dialogBuilder.create()
        b.show()
    }

    companion object {
        val REQUEST_CODE = 99
    }
}
