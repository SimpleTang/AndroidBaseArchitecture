package com.nacai.common.config

import android.app.Application
import android.content.Context
import android.util.Log
import com.nacai.base_lib.integration.AppLifeCycles
import com.nacai.base_lib.network.NetConfig
import com.nacai.base_lib.network.NetManager

object AppLifecycleInject : AppLifeCycles {
    override fun attachBaseContext(base: Context) {

    }

    override fun onCreate(application: Application) {
        NetManager.netConfig = NetConfig.Builder()
            .setBaseUrl("")
            .setRequestHandler(CommonNetRequestHandler())
            .build()

        Log.e("AppConfig", "onCreate: common" )
    }

    override fun onTerminate(application: Application) {

    }
}