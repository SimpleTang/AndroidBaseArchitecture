package com.nacai.base_lib.network

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nacai.base_lib.base.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

@Suppress("CAST_NEVER_SUCCEEDS")
fun BaseViewModel.requestNetApi(
    bindStatus: Boolean = false,
    isRefresh: Boolean = true,
    showToast: Boolean = true,
    error: Throwable.() -> Unit = {},
    finally: () -> Unit = {},
    call: suspend () -> Unit
): Job {
    return viewModelScope.launch {
        if (bindStatus) {
            //注册前先判断是否显示加载loading
            if (pageStatusEvent.value?.peekContent() != PageStatus.CONTENT) {
                pageStatusEvent.value = event(PageStatus.LOADING)
            }
        }
        try {
            call()
            if (bindStatus) {
                //成功
                if (pageStatusEvent.value?.peekContent() != PageStatus.CONTENT)
                    pageStatusEvent.value = event(PageStatus.CONTENT)
                //给列表设置是刷新还是加载更多
                if (isRefresh) {
                    refreshStatusEvent.value = event(RefreshStatus.REFRESH_SUCCESS)
                } else {
                    refreshStatusEvent.value = event(RefreshStatus.LOAD_MORE_SUCCESS)
                }
            }
        } catch (e: Throwable) {
            if (bindStatus) {
                //页面状态
                if (pageStatusEvent.value?.peekContent() != PageStatus.CONTENT) {
                    if (e is EmptyException) {
                        pageStatusEvent.value = event(PageStatus.EMPTY)
                    } else if (e is UnknownHostException || e is ConnectException) {
                        pageStatusEvent.value = event(PageStatus.NO_NETWORK)
                    } else {
                        e.printStackTrace()
                        pageStatusEvent.value = event(PageStatus.ERROR)
                    }
                }
                //refresh状态
                if (isRefresh) {
                    if (e is EmptyException) {
                        refreshStatusEvent.value = event(RefreshStatus.EMPTY)
                    } else {
                        refreshStatusEvent.value = event(RefreshStatus.REFRESH_FAIL)
                    }
                } else {
                    if (e is EmptyException) {
                        refreshStatusEvent.value = event(RefreshStatus.NOT_MORE)
                    } else {
                        refreshStatusEvent.value = event(RefreshStatus.LOAD_MORE_FAIL)
                    }
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
    call: suspend () -> Unit): Job {
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