package com.nacai.base_lib.network

import com.blankj.utilcode.util.AppUtils
import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter

class NetConfig private constructor(private val builder: Builder) {
    val connectTimeoutSecs: Long get() = builder.connectTimeoutSecs
    val readTimeoutSecs: Long get() = builder.readTimeoutSecs
    val writeTimeoutSecs: Long get() = builder.writeTimeoutSecs
    val logEnable: Boolean get() = builder.logEnable
    val logTag: String get() = builder.logTag
    val requestHandler: RequestHandler? get() = builder.requestHandler
    val cookieJar: CookieJar? get() = builder.cookieJar
    val interceptors: ArrayList<Interceptor> get() = builder.interceptors
    val clientBuilderInterceptor: ((OkHttpClient.Builder) -> OkHttpClient.Builder)? get() = builder.clientBuilderInterceptor
    val retrofitConverterFactory: ArrayList<Converter.Factory> get() = builder.retrofitConverterFactory
    val retrofitCallAdapterFactory: ArrayList<CallAdapter.Factory> get() = builder.retrofitCallAdapterFactory
    val baseUrl: String? get() = builder.baseUrl

    class Builder {
        var connectTimeoutSecs: Long = 30
        var readTimeoutSecs: Long = 30
        var writeTimeoutSecs: Long = 30
        var logEnable: Boolean = AppUtils.isAppDebug()
        var logTag: String = "Net"
        var requestHandler: RequestHandler? = null
        var cookieJar: CookieJar? = null
        var interceptors: ArrayList<Interceptor> = ArrayList()
        var clientBuilderInterceptor: ((OkHttpClient.Builder) -> OkHttpClient.Builder)? = null
        var retrofitConverterFactory: ArrayList<Converter.Factory> = ArrayList()
        var retrofitCallAdapterFactory: ArrayList<CallAdapter.Factory> = ArrayList()
        var baseUrl: String? = null

        /**
         * 连接超时时长
         * @param second 秒
         */
        fun setConnectTimeout(second: Long): Builder {
            connectTimeoutSecs = second
            return this
        }

        /**
         * 读超时时长
         * @param second 秒
         */
        fun setReadTimeout(second: Long): Builder {
            readTimeoutSecs = second
            return this
        }

        /**
         * 写超时时长
         * @param second 秒
         */
        fun setWriteTimeout(second: Long): Builder {
            writeTimeoutSecs = second
            return this
        }

        /**
         * 设置 log 日志
         */
        fun setLogEnable(enable: Boolean, tag: String = "Net"): Builder {
            logEnable = enable
            logTag = tag
            return this
        }

        /**
         * 设置网络拦截器
         */
        fun setRequestHandler(handler: RequestHandler): Builder {
            requestHandler = handler
            return this
        }

        /**
         * 设置 cookie 持久
         */
        fun setCookieJar(cookieJar: CookieJar): Builder {
            this.cookieJar = cookieJar
            return this
        }

        /**
         * 添加拦截器
         * 可以设置一些第三方的拦截器等
         */
        fun addInterceptor(interceptor: Interceptor): Builder {
            interceptors.add(interceptor)
            return this
        }

        fun addRetrofitConverterFactory(factory: Converter.Factory): Builder {
            retrofitConverterFactory.add(factory)
            return this
        }

        fun addRetrofitCallAdapterFactory(factory: CallAdapter.Factory): Builder {
            retrofitCallAdapterFactory.add(factory)
            return this
        }

        /**
         * 设置 HttpClientBuilder 的拦截器，用于自定义的一些配置等
         * 比如 https 的设置
         */
        fun setHttpClientBuilderInterceptor(interceptor: (OkHttpClient.Builder) -> OkHttpClient.Builder): Builder {
            clientBuilderInterceptor = interceptor
            return this
        }

        fun setBaseUrl(url: String): Builder {
            baseUrl = url
            return this
        }

        fun build() = NetConfig(this)
    }
}