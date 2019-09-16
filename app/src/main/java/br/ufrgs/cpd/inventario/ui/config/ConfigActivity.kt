package br.ufrgs.cpd.inventario.ui.config

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import br.ufrgs.cpd.inventario.R
import br.ufrgs.cpd.inventario.ui.login.LoginActivity
import br.ufrgs.cpd.inventario.ui.tutorial.TutorialActivity
import br.ufrgs.cpd.inventario.utils.LayoutUtils
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager
import br.ufrgs.ufrgsapi.user_data.UfrgsUser
import br.ufrgs.ufrgsapi.user_data.UfrgsUserDataManager
import kotlinx.android.synthetic.main.activity_config.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton
import st.lowlevel.storo.Storo
import java.util.concurrent.TimeUnit


/**
 * Created by Theo on 08/12/2017.
 */
class ConfigActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)
        LayoutUtils.setupToolbar(this, toolbar, null, true)
        LayoutUtils.setStatusBarColor(this, "#6791ce")

        if(Storo.contains("user")){
            val user = Storo.get<UfrgsUser>("user", UfrgsUser::class.java).execute()
            val foto = UfrgsUserDataManager.convertPicture(user)
            profile_image.setImageBitmap(foto)
            txtName.text = user?.nomePessoa
            txtNumber.text = user?.codPessoa

        } else {

            UfrgsUserDataManager(this).getData(object : UfrgsUserDataManager.OnDataCallback {
                override fun onDataReady(user: UfrgsUser?) {
                    user?.let {
                        Storo.put("user", it).setExpiry(6, TimeUnit.DAYS).execute()
                        val foto = UfrgsUserDataManager.convertPicture(user)
                        profile_image.setImageBitmap(foto)
                        txtName.text = user.nomePessoa
                        txtNumber.text = user.codPessoa
                    }
                }

                override fun onError(error: String?) {
                    //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    return
                }

            })
        }

        btnLogout.onClick {
            alert("Você tem certeza que deseja sair?") {
                yesButton {
                    Storo.delete("user")
                    UfrgsTokenManager().removeToken(this@ConfigActivity)
                    startActivity<LoginActivity>()
                    finish()
                }
                noButton {}
            }.show()
        }

        btn_tutorial.onClick {
            startActivity<TutorialActivity>()
        }
    }
}