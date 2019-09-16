package br.ufrgs.cpd.inventario.ui.main

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import br.ufrgs.cpd.inventario.R
import br.ufrgs.cpd.inventario.models.EspacosFisico
import br.ufrgs.cpd.inventario.models.Orgao
import br.ufrgs.cpd.inventario.models.Patrimonio
import br.ufrgs.cpd.inventario.models.Predio
import br.ufrgs.cpd.inventario.ui.config.ConfigActivity
import br.ufrgs.cpd.inventario.ui.edit.EditActivity
import br.ufrgs.cpd.inventario.ui.search.SearchActivity
import br.ufrgs.cpd.inventario.utils.LayoutUtils
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick


class MainActivity : AppCompatActivity(), MainScreenContract.View {

    private var mSelectedOrgao: Orgao? = null
    private var mSelectedPredio: Predio? = null
    private var mSelectedEspacoFisico: EspacosFisico? = null
    private var progressDialog: ProgressDialog? = null

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
                startActivityForResult<SearchActivity>(REQUEST_CODE, "op" to "orgao")
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
                mSelectedEspacoFisico = null
                startActivityForResult(intent, REQUEST_CODE, options.toBundle())
            } else {
                startActivityForResult<SearchActivity>(REQUEST_CODE, "op" to "predio")
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
                    mSelectedPredio?.let { intent.putExtra("predio", it.CodEspacoFisico) }
                    startActivityForResult(intent, REQUEST_CODE, options.toBundle())
                } else {
                    val i = Intent(this, SearchActivity::class.java)
                    i.putExtra("op", "espaco_fisico")
                    mSelectedPredio?.let { i.putExtra("predio", it.CodEspacoFisico) }
                    startActivityForResult(i, REQUEST_CODE)
                }

            } catch (e: Exception) {
                toast("Você precisa selecionar o orgão e prédio antes de selecionar o espaço físico")
            }
        }

        btn3.setOnLongClickListener {
            toast(btn3_txt.text.toString())
            return@setOnLongClickListener true
        }

        btn_barcode.setOnClickListener {
            if (validateSelections()) {
                val integrator = IntentIntegrator(this)
                integrator.setOrientationLocked(false)
                //integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
                integrator.setPrompt("Escaneie um código de barras")
                integrator.setCameraId(0)  // Use a specific camera of the device
                integrator.setBeepEnabled(true)
                integrator.setBarcodeImageEnabled(true)
                integrator.initiateScan()
            } else {
                toast("Você precisa selecionar orgão, prédio e espaço físico")
            }

        }

        btn_digit.setOnClickListener {
            if (validateSelections()) {
                searchByNumberDialog()
            } else {
                toast("Você precisa selecionar orgão, prédio e espaço físico")
            }
        }

        btn_settings.onClick {
            startActivity<ConfigActivity>()
        }

        /*btn_ntr.onClick {
            if (validateSelections()) {
                EditActivity.start(this@MainActivity, EditActivity.TYPE_NTR, mSelectedOrgao!!.CodOrgao, mSelectedOrgao!!.NomeOrgao, mSelectedPredio!!.CodPredio, mSelectedEspacoFisico!!.codEspacoFisico)
            } else {
                toast("Você precisa selecionar orgão, prédio e espaço físico")
            }
        }*/

        btn_no_plate.onClick {
            if (validateSelections()) {
                EditActivity.startNoPlate(this@MainActivity, EditActivity.TYPE_NO_PLATE, mSelectedOrgao!!.CodOrgao, mSelectedOrgao!!.NomeOrgao, mSelectedPredio!!.CodPredio, mSelectedEspacoFisico!!.codEspacoFisico)
            } else {
                toast("Você precisa selecionar orgão, prédio e espaço físico")
            }
        }

    }

    override fun showMessage(string: String) {
        progressDialog?.dismiss()
        toast(string)
    }

    override fun showNtrMessage(string: String, nrPatrimonio: String) {
        progressDialog?.dismiss()

        alert(string) {
            yesButton { d ->
                d.dismiss()
                if (string == "Bem não tem registro (NTR)") {
                    EditActivity.startNtr(this@MainActivity, nrPatrimonio, EditActivity.TYPE_NTR, mSelectedOrgao!!.CodOrgao, mSelectedOrgao!!.NomeOrgao, mSelectedPredio!!.CodPredio, mSelectedEspacoFisico!!.codEspacoFisico)
                }
            }
        }.show()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            //QRCODE
            if (result.contents == null) {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show()
            } else {
                progressDialog?.setMessage("Carregando...")
                progressDialog?.show()
                MainScreenPresenter(this, this).getItem(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == REQUEST_CODE && data != null) {
                //VEIO da tela de selecao
                when {
                    data.hasExtra("orgao") -> {
                        mSelectedOrgao = data.getSerializableExtra("orgao") as Orgao
                        cv2.visibility = View.VISIBLE
                    }
                    data.hasExtra("predio") -> {
                        mSelectedPredio = data.getSerializableExtra("predio") as Predio
                        cv3.visibility = View.VISIBLE
                    }
                    data.hasExtra("espaco_fisico") -> {
                        mSelectedEspacoFisico = data.getSerializableExtra("espaco_fisico") as EspacosFisico
                    }
                }

                updateButtonText()
            }
        }
    }

    override fun onItemReady(patrimonio: Patrimonio) {
        progressDialog?.dismiss()

        if (validateSelections()) {
            EditActivity.startPlate(this,
                    EditActivity.TYPE_PLATE,
                    patrimonio,
                    mSelectedOrgao!!.CodOrgao,
                    mSelectedOrgao!!.NomeOrgao,
                    mSelectedPredio!!.CodPredio,
                    mSelectedEspacoFisico!!.codEspacoFisico)
        }
    }

    private fun updateButtonText() {
        btn1_txt.text = if (mSelectedOrgao != null) mSelectedOrgao!!.NomeOrgao else "Órgão"
        btn2_txt.text = if (mSelectedPredio != null) mSelectedPredio!!.NomePredio else "Prédio"
        btn3_txt.text = if (mSelectedEspacoFisico != null) mSelectedEspacoFisico!!.denominacaoEspacoFisico else "Espaço Físico"
    }

    private fun searchByNumberDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_patrimonio, null)
        dialogBuilder.setView(dialogView)

        val et = dialogView.findViewById<EditText>(R.id.edit1)
        et.inputType = InputType.TYPE_CLASS_NUMBER

        dialogBuilder.setMessage("Entre com o número de patrimônio")
        dialogBuilder.setPositiveButton("Ok") { _, _ ->
            progressDialog?.setMessage("Carregando...")
            progressDialog?.show()
            MainScreenPresenter(this, this).getItem(et.text.toString())
        }

        dialogBuilder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        val b = dialogBuilder.create()
        b.show()
    }

    private fun validateSelections(): Boolean = mSelectedOrgao != null && mSelectedPredio != null && mSelectedEspacoFisico != null

    companion object {
        val REQUEST_CODE = 99
    }
}
