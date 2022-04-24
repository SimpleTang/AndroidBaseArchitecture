package com.tyl.base_lib.network

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.requestNetApi(
    showToast: Boolean = true,
    error: Throwable.() -> Unit = {},
    finally: () -> Unit = {},
    call: suspend () -> Unit
): Job {
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