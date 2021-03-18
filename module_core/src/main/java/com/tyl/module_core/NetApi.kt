package com.tyl.module_core

import androidx.lifecycle.MutableLiveData
import com.nacai.base_lib.base.BaseViewModel
import com.nacai.base_lib.base.VMEvent
import com.nacai.base_lib.network.NetManager
import com.nacai.base_lib.network.doLeakedRequest
import com.nacai.base_lib.network.requestNetApi
import com.nacai.base_lib.widget.multistate.PageStatus
import com.nacai.base_lib.widget.refresh.RefreshStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

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
