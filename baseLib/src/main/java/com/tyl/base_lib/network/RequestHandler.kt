package com.tyl.base_lib.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import kotlin.jvm.Throws

/***
 *
 * 网络请求拦截器
 */

interface RequestHandler {
    /**
     * 发起请求拦截方法
     */
    fun onBeforeRequest(request: Request, chain: Interceptor.Chain): Request

    /**
     * 请求结果拦截方法
     */
    @Throws(Throwable::class)
    fun onAfterRequest(response: Response, chain: Interceptor.Chain): Response

    /**
     * 接口错误回调的拦截处理
     * 这里可以做一些项目中通用的错误处理，比如显示 Toast，挤下线等
     * @return 是否拦截回调，返回 true 则此次请求的 onError 不会被回调
     */
    fun onErrorCall(e: Throwable, showToast: Boolean): Boolean
}