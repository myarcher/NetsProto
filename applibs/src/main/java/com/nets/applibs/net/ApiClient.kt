package com.nets.applibs.net

import android.util.Log
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.nets.applibs.glides.HttpsUtil
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    lateinit var retrofit: Retrofit

    private object Holder {
        val INSTANCE = ApiClient()
    }

    companion object {
        val instance by lazy { Holder.INSTANCE }
    }

    //懒加载
    fun init(build: Builder): ApiClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(build.connectTimeout, TimeUnit.SECONDS)
            .readTimeout(build.readTimeout, TimeUnit.SECONDS)
            .writeTimeout(build.writeTimeout, TimeUnit.SECONDS)
        val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.i(build.logTag, message)//这里打印的是网络请求过程的信息
            }
        })
        val sslParams = HttpsUtil.getSslSocketFactory()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(HttpParamInterceptor())
            .addInterceptor(logging)
            .cookieJar(CookieJarImpl())
            .sslSocketFactory(sslParams!!.sSLSocketFactory!!, sslParams.trustManager!!)
        //配置retrofit
        retrofit = Retrofit.Builder()
            .baseUrl(build.baseUrl) //基本的url
            .addConverterFactory(GsonConverterFactory.create()) //使用gson
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//使用rxjava
            .client(builder.build())
            .build()
        return this
    }

    fun <T> getServer(serviceClass: Class<T>?): T {
        return retrofit.create(serviceClass)
    }
}

class Builder {
    var connectTimeout = 15L;
    var readTimeout = 15L;
    var writeTimeout = 15L;
    var logTag = "http_log";
    var baseUrl = "";

    constructor()
    constructor(url: String) {
        this.baseUrl = url
    }

    fun setConnectTime(connectTimeout: Long): Builder {
        this.connectTimeout = connectTimeout
        return this
    }

    fun setReadTime(readTimeout: Long): Builder {
        this.readTimeout = readTimeout
        return this
    }

    fun setWriteTime(writeTimeout: Long): Builder {
        this.writeTimeout = writeTimeout
        return this
    }

    fun setLogTag(logTag: String): Builder {
        this.logTag = logTag
        return this
    }

    fun setBaseUrl(baseUrl: String): Builder {
        this.baseUrl = baseUrl
        return this
    }

    fun build():ApiClient{
        val api = ApiClient.instance
       return  api.init(this)
    }
}