package com.nacai.base_lib.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/***
 * 网络拦截器
 */

class NetInterceptor(private val handler: RequestHandler) : Interceptor {
    /**
     * 网络请求拦截方法
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = handler.onBeforeRequest(request, chain)
        val response = chain.proceed(request)
        return handler.onAfterRequest(response, chain)
    }

}