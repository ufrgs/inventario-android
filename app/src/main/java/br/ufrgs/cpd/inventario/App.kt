package br.ufrgs.cpd.inventario

import android.app.Application
import androidx.multidex.MultiDexApplication
import br.ufrgs.ufrgsapi.UfrgsAPI
import st.lowlevel.storo.StoroBuilder

/**
 * Created by Theo on 26/09/2017.
 */
class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        UfrgsAPI.initialize(setToProduction, getClientId(), getClientSecret(), scope, grantType)

        StoroBuilder.configure(18192)  // maximum size to allocate in bytes
                .setDefaultCacheDirectory(this)
                .initialize()
    }


    companion object {

        private const val setToProduction = true

        fun getClientId(): String =
                when (setToProduction) {
                    true -> clientIdProd
                    false -> clientIdDev
                }


        fun getClientSecret(): String =
                when (setToProduction) {
                    true -> clientSecretProd
                    false -> clientSecretDev
                }


        fun getBaseUrl(): String =
                when (setToProduction) {
                    true -> BASE_URL_PROD
                    false -> BASE_URL_DEV
                }


        const val BASE_URL_DEV = ""
        const val BASE_URL_PROD = ""

        const val clientIdDev = ""
        const val clientSecretDev = ""

        const val clientIdProd = ""
        const val clientSecretProd = ""

        const val scope = ""
        const val grantType = ""

        const val KEY_TUTORIAL = "Tutorial"
    }
}
