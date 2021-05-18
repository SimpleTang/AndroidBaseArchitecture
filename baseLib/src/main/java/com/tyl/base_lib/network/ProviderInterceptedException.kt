package com.tyl.base_lib.network

import java.io.IOException

/***
 * 被拦截器拦截的异常，触发此异常将中断整个网络回调，onError 不会被调用
 */
open class ProviderInterceptedException(message: String, val code: Int = 200) : IOException(message)