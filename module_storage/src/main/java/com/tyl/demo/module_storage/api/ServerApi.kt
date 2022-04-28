package com.tyl.demo.module_storage.api

import com.tyl.demo.common.bean.BaseBean
import com.tyl.demo.common.bean.ConfigRES
import com.tyl.demo.module_storage.BuildConfig
import com.tyl.process_annotation.NetApi
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

@NetApi(host = BuildConfig.SERVER_HOST)
interface ServerApi {
    /**
     * 获取麦上配置信息
     * @return
     */
    @POST("/msmember/getConfigByTypes")
    @FormUrlEncoded
    suspend fun getConfigByTypes(@Field("config_type") configTypesJsonArrayStr: String): BaseBean<ConfigRES>
}