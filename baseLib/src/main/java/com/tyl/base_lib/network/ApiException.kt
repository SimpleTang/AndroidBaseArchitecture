package com.tyl.base_lib.network

import java.io.IOException

/***
 * 网络请求错误异常类
 */

open class ApiException(message: String, val code: Int = 400) : IOException(message)