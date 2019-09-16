package br.ufrgs.cpd.inventario.ui.splash

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import br.ufrgs.cpd.inventario.ui.main.MainActivity
import br.ufrgs.cpd.inventario.R
import br.ufrgs.cpd.inventario.ui.login.LoginActivity
import br.ufrgs.cpd.inventario.utils.LayoutUtils
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager
import org.jetbrains.anko.startActivity

/**
 * Created by Theo on 18/09/2017.
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        LayoutUtils.setStatusBarColor(this, "#6791ce")

        Handler().postDelayed({
            if(UfrgsTokenManager().getToken(this) != null){
                startActivity<MainActivity>()
            } else {
                startActivity<LoginActivity>()
            }

            finish()
        }, (1.5 * 1000).toLong())
    }
}