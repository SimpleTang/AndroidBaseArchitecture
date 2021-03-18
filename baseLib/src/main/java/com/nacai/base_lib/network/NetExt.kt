package com.nacai.base_lib.network

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nacai.base_lib.base.*
import com.nacai.base_lib.widget.multistate.PageStatus
import com.nacai.base_lib.widget.refresh.RefreshStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

@Suppress("CAST_NEVER_SUCCEEDS")
fun BaseViewModel.requestNetApi(
    showToast: Boolean = true,
    isRefresh: Boolean = true,
    refreshStatus: MutableLiveData<VMEvent<RefreshStatus>>? = refreshStatusEvent,
    pageStatus: MutableLiveData<VMEvent<PageStatus>>? = pageStatusEvent,
    error: Throwable.() -> Unit = {},
    finally: () -> Unit = {},
    call: suspend () -> Unit
): Job {
    return viewModelScope.launch {
        //注册前先判断是否显示加载loading
        pageStatus?.value = event(PageStatus.LOADING)
        try {
            call()
            //成功
            pageStatus?.value = event(PageStatus.CONTENT)
            //给列表设置是刷新还是加载更多
            if (isRefresh) {
                refreshStatus?.value = event(RefreshStatus.REFRESH_SUCCESS)
            } else {
                refreshStatus?.value = event(RefreshStatus.LOAD_MORE_SUCCESS)
            }
        } catch (e: Throwable) {
            //页面状态
            if (e is UnknownHostException || e is ConnectException) {
                pageStatus?.value = event(PageStatus.NO_NETWORK)
            } else {
                e.printStackTrace()
                pageStatus?.value = event(PageStatus.ERROR)
            }

            //refresh状态
            if (isRefresh) {
                if (e is EmptyException) {
//                    refreshStatus?.value = event(RefreshStatus.EMPTY)
                    pageStatus?.value = event(PageStatus.EMPTY)
                } else {
                    refreshStatus?.value = event(RefreshStatus.REFRESH_FAIL)
                }
            } else {
                if (e is EmptyException) {
                    refreshStatus?.value = event(RefreshStatus.NOT_MORE)
                } else {
                    refreshStatus?.value = event(RefreshStatus.LOAD_MORE_FAIL)
                }
            }

            if (NetManager.netConfig.requestHandler?.onErrorCall(e, showToast) != true) {
                error(e)
            }
        } finally {
            finally()
        }
    }
}


fun Any.doLeakedRequest(
    context: CoroutineContext = Dispatchers.Main,
    showToast: Boolean = true,
    error: Throwable.() -> Unit = {},
    finally: () -> Unit = {},
    call: suspend () -> Unit
): Job {
    return CoroutineScope(context).launch {
        try {
            call()
        } catch (e: Throwable) {
            if (NetManager.netConfig.requestHandler?.onErrorCall(e, showToast) != true) {
                error(e)
            }
        } finally {
            finally()
        }
    }

}