package com.nacai.common.config

import com.blankj.utilcode.util.StringUtils
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.nacai.base_lib.network.ApiException
import com.nacai.base_lib.network.EmptyException
import com.nacai.base_lib.network.RequestHandler
import com.nacai.base_lib.tools.L
import com.nacai.common.bean.BaseBean
import com.nacai.common.bean.HttpException
import com.nacai.common.tools.MMKVUtils
import com.nacai.common.tools.MyToast
import com.nacai.common_base.BuildConfig
import com.nacai.common_base.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.charset.Charset

class CommonNetRequestHandler : RequestHandler {
    override fun onBeforeRequest(request: Request, chain: Interceptor.Chain): Request {
        val newRequest = request.newBuilder()

        // 将 POST 的 field 转化成 JsonObject
        if (request.method == "POST" && request.body is FormBody) {
            val formBody = request.body as FormBody
            val params = JsonObject()
            for (i in 0 until formBody.size) {
                val key = formBody.name(i)
                val value = formBody.value(i)
                params.addProperty(key, value)
            }
            val requestBody: RequestBody = params.toString()
                .toRequestBody("application/json;charset=utf-8".toMediaTypeOrNull())
            newRequest
                .post(requestBody)
        }

        // 添加用户 token
        newRequest
            .addHeader("Authorization", "Bearer " + MMKVUtils.getCurrentUserToken())
            .addHeader("Content-Type", "application/json; charset=UTF-8")
        return newRequest.build()
    }

    override fun onAfterRequest(response: Response, chain: Interceptor.Chain): Response {
        if (response.isSuccessful) {
            val source = response.body?.source()
            source?.request(Long.MAX_VALUE)
            val buffer = source?.buffer
            val body = buffer?.clone()?.readString(Charset.forName("UTF-8"))
            if (StringUtils.isEmpty(body)) {
                throw EmptyException()
            }
            val bean: BaseBean<*> = try {
                Gson().fromJson(body, object : TypeToken<BaseBean<*>>() {}.type)
            } catch (e: Exception) {
                throw ApiException(e.message
                    ?: StringUtils.getString(R.string.common_net_server_error))
            }
            if (bean.code == 200) {
                try {
                    return response
                        .newBuilder()
                        .body(
                            Gson().toJson(bean)
                                .toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull())
                        )
                        .build()
                } catch (e: Exception) {
                    throw ApiException(e.message
                        ?: StringUtils.getString(R.string.common_net_server_error))
                }
            } else {
                throw HttpException(bean)
            }
        } else {
            throw ApiException(response.message, response.code)
        }
    }

    override fun onErrorCall(e: Throwable, showToast: Boolean): Boolean {
        if (BuildConfig.DEBUG) {
            e.printStackTrace()
        }
        if (showToast) {
            if (e is EmptyException) {
                L.e("HTTP Response <---", "数据为空")
            } else if (e is HttpException) {
                e.baseBean.message.let { MyToast.showToast(it) }
            } else if (e is ApiException) {
                e.message?.let { MyToast.showToast(it) }
            } else if (e is SocketTimeoutException) {
                MyToast.showToast(StringUtils.getString(R.string.common_net_time_out))
            } else if (e is UnknownHostException || e is ConnectException) {
                MyToast.showToast(StringUtils.getString(R.string.common_net_disconnect))
            } else if (e is JSONException || e is JsonParseException) {
                MyToast.showToast(StringUtils.getString(R.string.common_net_data_error))
                e.printStackTrace()
            } else {
                L.e("HTTP Response <---", "请求失败 ${e.message}")
            }
        }
        return if (e is HttpException) {
            e.baseBean.code.let { code ->
                if (code == 401 || code == 700 || code == 403) {
                    if (MMKVUtils.getCurrentUserID().isNullOrEmpty()) {
                        MMKVUtils.removeUserInfoByCache() // 清除 token 等 用户数据
                    }
                    // todo 关闭其他所有页面并退出登录
//                    NavigationUtils.goLoginActivityClearAll()
                    true
                } else {
                    false
                }
            }
        } else false
    }
}