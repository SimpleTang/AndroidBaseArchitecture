package com.tyl.module_core

import com.tyl.base_lib.network.NetManager

private val apiServer by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { NetManager["1", ApiServer::class.java] }

////
//fun BaseViewModel.api(
//    showToast: Boolean = true,
//    isRefresh: Boolean = true,
//    refreshStatus: MutableLiveData<VMEvent<RefreshStatus>>? = refreshStatusEvent,
//    pageStatus: MutableLiveData<VMEvent<PageStatus>>? = pageStatusEvent,
//    error: Throwable.() -> Unit = {},
//    finally: () -> Unit = {},
//    call: suspend ApiServer.() -> Unit
//): Job {
//    return requestNetApi(
//        showToast,
//        isRefresh,
//        refreshStatus,
//        pageStatus,
//        error,
//        finally,
//        { call(apiServer) })
//}
////
//fun Any.leakedApi(
//    context: CoroutineContext = Dispatchers.Main,
//    showToast: Boolean = true,
//    error: Throwable.() -> Unit = {},
//    finally: () -> Unit = {},
//    call: suspend ApiServer.() -> Unit
//): Job {
//    return doLeakedRequest(context, showToast, error, finally, { call(apiServer) })
//}
