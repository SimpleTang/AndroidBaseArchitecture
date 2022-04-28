package com.tyl.demo.common.config

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.tyl.base_lib.integration.AppLifeCycles
import com.tyl.base_lib.network.NetConfig
import com.tyl.base_lib.network.NetManager
import com.tyl.base_lib.network.NullStringToEmptyAdapterFactory
import com.tyl.base_lib.widget.multiplestatus.MultipleStatusLayout
import com.tyl.demo.common.R
import retrofit2.converter.gson.GsonConverterFactory

object AppLifecycleInject : AppLifeCycles {
    override fun attachBaseContext(base: Context) {

    }

    override fun onCreate(application: Application) {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .registerTypeAdapterFactory(NullStringToEmptyAdapterFactory())
            .create()
        NetManager.netConfig = NetConfig.Builder()
            .setBaseUrl("")
            .addRetrofitConverterFactory(GsonConverterFactory.create(gson))
            .setRequestHandler(CommonNetRequestHandler())
            .build()

        Log.e("AppConfig", "onCreate: common" )

        MultipleStatusLayout
            .setDefaultEmptyViewLayoutId(R.layout.common_default_empty_view)
            .setDefaultInitViewLayoutId(R.layout.common_default_empty_view)
            .setDefaultLoadingViewLayoutId(R.layout.common_default_empty_view)
            .setDefaultErrorViewLayoutId(R.layout.common_default_load_error_view)
    }

    override fun onTerminate(application: Application) {

    }
}