package com.nacai.base_lib.network

import java.io.IOException

/***
 * 网络请求错误异常类
 */

open class ApiException(message: String, val code: Int = 200) : IOException(message)