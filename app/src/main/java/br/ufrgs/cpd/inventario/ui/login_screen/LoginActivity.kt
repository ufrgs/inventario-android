package br.ufrgs.cpd.inventario.ui.login_screen

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import br.ufrgs.cpd.inventario.R
import br.ufrgs.cpd.inventario.ui.main_screen.MainActivity
import br.ufrgs.cpd.inventario.utils.LayoutUtils
import br.ufrgs.ufrgsapi.token.UfrgsToken
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager
import kotlinx.android.synthetic.main.activity_login_new.*
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


/**
 * Created by Theo on 18/09/2017.
 */
class LoginActivity : AppCompatActivity() {

    lateinit var mProgressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_new)
        LayoutUtils.setStatusBarColor(this, "#6791ce")
        LayoutUtils.setNavbarTranslucent(this)

        val h = LayoutUtils.getScreenHeight() * 0.55
        image_top.layoutParams.height = h.toInt()


        btn_login.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(login_layout_password.windowToken, 0)

            val u = login_card.text.toString().isEmpty()
            val p = login_password_et.text.toString().isEmpty()

            if (u) {
                login_layout_card.isErrorEnabled = true
                login_layout_card.error = "Informe o cartão da UFRGS"
            }
            if (p) {
                login_layout_password.isErrorEnabled = true
                login_layout_password.error = "Informe a senha"
            }

            if (!u && !p)
                getTokenFromAPI(login_card.text.toString(), login_password_et.text.toString())
        }

    }

    private fun getTokenFromAPI(user: String, password: String) {

        mProgressDialog = indeterminateProgressDialog("Carregando...")

        UfrgsTokenManager().requestNewToken(this, user, password, object : UfrgsTokenManager.OnTokenListener {
            override fun onTokenReady(token: UfrgsToken) {
                mProgressDialog.dismiss()

                val prefs = PreferenceManager.getDefaultSharedPreferences(this@LoginActivity)
                val editor = prefs.edit()
                editor.putString("cartao", user)
                editor.apply()


                toast("Bem-vindo")
                startActivity<MainActivity>()
                finish()

            }

            override fun onError(error: String) {
                mProgressDialog.dismiss()
                if (error.contains("Unauthorized")) {
                    toast(getString(R.string.wrong_user_or_password))
                } else {
                    //mListener.onLoginError(error);
                }
            }
        })
    }
}