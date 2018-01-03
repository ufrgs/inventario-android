package br.ufrgs.cpd.inventario

import android.app.Application
import br.ufrgs.ufrgsapi.UfrgsAPI
import st.lowlevel.storo.StoroBuilder

/**
 * Created by Theo on 26/09/2017.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        UfrgsAPI.initialize(true, clientId, clientSecret, scope, grantType)

        StoroBuilder.configure(18192)  // maximum size to allocate in bytes
                .setDefaultCacheDirectory(this)
                .initialize()
    }

    companion object {
        val clientId = ""
        val clientSecret = ""
        val scope = ""
        val grantType = ""
    }
}