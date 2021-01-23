package com.nacai.common.config

import com.nacai.base_lib.network.RequestHandler
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class CommonNetRequestHandler:RequestHandler {
    override fun onBeforeRequest(request: Request, chain: Interceptor.Chain): Request {
        TODO("Not yet implemented")
    }

    override fun onAfterRequest(response: Response, chain: Interceptor.Chain): Response {
        TODO("Not yet implemented")
    }

    override fun onErrorCall(e: Throwable, showToast: Boolean): Boolean {
        return false
    }
}