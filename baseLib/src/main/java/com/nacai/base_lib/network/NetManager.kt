package com.nacai.base_lib.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/***
 * 网络请求封装类
 * 使用Retrofit+OkHttp
 */

object NetManager {

    //存放retrofit的map
    private val retrofitMap = HashMap<String, Retrofit>()

    var netConfig: NetConfig = NetConfig.Builder().build()

    /**
     * 获取retrofit实例
     */
    fun getRetrofit(baseUrl: String): Retrofit {
        if ((baseUrl.isEmpty())) {
            throw IllegalStateException("baseUrl can not be null")
        }
        if (retrofitMap[baseUrl] != null) {
            return retrofitMap[baseUrl]!!
        }

        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getClient())

        netConfig.retrofitCallAdapterFactory.forEach {
            builder.addCallAdapterFactory(it)
        }

        netConfig.retrofitConverterFactory.forEach {
            builder.addConverterFactory(it)
        }

        val retrofit = builder.build()
        retrofitMap[baseUrl] = retrofit
        return retrofit
    }

    /**
     * 获取OkHttpClient实例
     */
    private fun getClient(): OkHttpClient {
        var builder = OkHttpClient.Builder()

        builder.connectTimeout(netConfig.connectTimeoutSecs, TimeUnit.SECONDS)

        builder.readTimeout(netConfig.readTimeoutSecs, TimeUnit.SECONDS)

        builder.writeTimeout(netConfig.writeTimeoutSecs, TimeUnit.SECONDS)

        val cookieJar = netConfig.cookieJar
        if (cookieJar != null) {
            builder.cookieJar(cookieJar)
        }

        netConfig.clientBuilderInterceptor?.let {
            builder = it.invoke(builder)
        }

        netConfig.requestHandler?.let {
            builder.addInterceptor(NetInterceptor(it))
        }

        netConfig.interceptors.forEach {
            builder.addInterceptor(it)
        }

        val loggingInterceptor = HttpLoggingInterceptor {
            if (netConfig.logEnable)
                Log.d(netConfig.logTag, it)
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)

        return builder.build()
    }

    /**
     * 获取具体网络请求Service的接口实例
     */
    operator fun <S> get(baseUrl: String, service: Class<S>): S {
        return getRetrofit(baseUrl).create(service)
    }

    operator fun <S> get(service: Class<S>): S {
        return getRetrofit(netConfig.baseUrl ?: "").create(service)
    }

}