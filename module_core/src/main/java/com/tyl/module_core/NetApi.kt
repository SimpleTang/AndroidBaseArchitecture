package com.tyl.module_core

import com.nacai.base_lib.base.BaseViewModel
import com.nacai.base_lib.network.NetManager
import com.nacai.base_lib.network.doLeakedRequest
import com.nacai.base_lib.network.requestNetApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

private val apiServer by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { NetManager["1", ApiServer::class.java] }
//
//fun BaseViewModel.api(
//    bindStatus: Boolean = true,
//    isRefresh: Boolean = true,
//    showToast: Boolean = true,
//    error: Throwable.() -> Unit = {},
//    finally: () -> Unit = {},
//    call: suspend ApiServer.() -> Unit
//) : Job {
//    return requestNetApi(bindStatus, isRefresh, showToast, error, finally, { call(apiServer) })
//}
//
//fun Any.leakedApi(
//    context: CoroutineContext = Dispatchers.Main,
//    showToast: Boolean = true,
//    error: Throwable.() -> Unit = {},
//    finally: () -> Unit = {},
//    call: suspend ApiServer.() -> Unit
//): Job {
//    return doLeakedRequest(context, showToast, error, finally, { call(apiServer) })
//}
