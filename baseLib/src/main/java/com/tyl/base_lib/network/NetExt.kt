package com.tyl.base_lib.network

<<<<<<< HEAD
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tyl.base_lib.base.*
import com.tyl.base_lib.widget.multistate.PageStatus
import com.tyl.base_lib.widget.refresh.RefreshStatus
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
    refreshStatus: MutableLiveData<RefreshStatus>? = this.defaultRefreshStatus,
    pageStatus: MutableLiveData<PageStatus>? = this.defaultPageStatus,
=======
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.requestNetApi(
    showToast: Boolean = true,
>>>>>>> v0.0.1
    error: Throwable.() -> Unit = {},
    finally: () -> Unit = {},
    call: suspend () -> Unit
): Job {
<<<<<<< HEAD
    return viewModelScope.launch {
        //注册前先判断是否显示加载loading
        pageStatus?.value = PageStatus.LOADING
        try {
            call()
            //成功
            pageStatus?.value = PageStatus.CONTENT
            //给列表设置是刷新还是加载更多
            if (isRefresh) {
                refreshStatus?.value = RefreshStatus.REFRESH_SUCCESS
            } else {
                refreshStatus?.value = RefreshStatus.LOAD_MORE_SUCCESS
            }
        } catch (e: Throwable) {
            //页面状态
            if (e is UnknownHostException || e is ConnectException) {
                pageStatus?.value = PageStatus.NO_NETWORK
            } else {
                e.printStackTrace()
                pageStatus?.value = PageStatus.ERROR
            }

            //refresh状态
            if (isRefresh) {
                if (e is EmptyException) {
//                    refreshStatus?.value = event(RefreshStatus.EMPTY)
                    pageStatus?.value = PageStatus.EMPTY
                } else {
                    refreshStatus?.value = RefreshStatus.REFRESH_FAIL
                }
            } else {
                if (e is EmptyException) {
                    refreshStatus?.value = RefreshStatus.NOT_MORE
                } else {
                    refreshStatus?.value = RefreshStatus.LOAD_MORE_FAIL
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
=======
    return launch {
        supervisorScope {
            launch(CoroutineExceptionHandler { _, throwable ->
                if (NetManager.netConfig.requestHandler?.onErrorCall(
                        throwable,
                        showToast
                    ) != true
                ) {
                    error(throwable)
                }
            }) {
                call()
            }
        }
        finally()
    }
}

suspend fun <T> suspendRequestNetApi(showToast: Boolean = true, call: suspend () -> T): T {
    var exception: Throwable? = null
    var result: T? = null
    supervisorScope {
        launch(CoroutineExceptionHandler { _, throwable ->
            exception = throwable
            NetManager.netConfig.requestHandler?.onErrorCall(throwable, showToast)
        }) {
            result = call()
        }
    }
    return result ?: throw exception ?: ApiException("未知异常")
}

fun doLeakRequest(
>>>>>>> v0.0.1
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