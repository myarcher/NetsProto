package com.nets.applibs.glides

import android.content.Context
import androidx.annotation.NonNull
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.nets.applibs.glides.HttpsUtil.UnSafeHostnameVerifier
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.TimeUnit

@GlideModule
class AppsGlideModule : AppGlideModule() {
    override fun registerComponents(
        @NonNull context: Context,
        @NonNull glide: Glide,
        @NonNull registry: Registry
    ) {
        super.registerComponents(context, glide, registry)
        val sslParams = HttpsUtil.getSslSocketFactory()
        val okhttpClient = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(60 * 1000.toLong(), TimeUnit.MILLISECONDS)
            .sslSocketFactory(sslParams!!.sSLSocketFactory!!, sslParams.trustManager!!)
            .hostnameVerifier(UnSafeHostnameVerifier())
            .build()
        registry.replace(
            GlideUrl::class.java, InputStream::class.java,
            OkHttpUrlLoader.Factory(okhttpClient)
        )
    }
}