package br.ufrgs.cpd.inventario.network

import android.content.Context
import br.ufrgs.cpd.inventario.App
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory





/**
 * Created by theolm on 05/02/2018.
 */
object ApiBuilder {

    fun retrofitBuilder(context : Context) : Retrofit {

        val cache = Cache(context.applicationContext.cacheDir, 10 * 1024 * 1024)
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(interceptor)
                .build()

        return Retrofit.Builder()
                .baseUrl(App.getBaseUrl())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}