package br.ufrgs.cpd.inventario.usecase

import android.content.Context
import android.content.Intent
import br.ufrgs.cpd.inventario.ui.login.LoginActivity
import br.ufrgs.ufrgsapi.token.UfrgsTokenManager
import org.jetbrains.anko.runOnUiThread
import st.lowlevel.storo.Storo

class LogoutUseCase(private val context: Context) {
    fun logout() {
        context.runOnUiThread {
            Storo.delete("user")
            UfrgsTokenManager().removeToken(context)
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

    }
}