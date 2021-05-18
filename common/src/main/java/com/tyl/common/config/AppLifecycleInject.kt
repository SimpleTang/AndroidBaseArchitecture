package com.tyl.common.config

import android.app.Application
import android.content.Context
import android.util.Log
import com.tyl.base_lib.integration.AppLifeCycles
import com.tyl.base_lib.network.NetConfig
import com.tyl.base_lib.network.NetManager
import com.tyl.base_lib.widget.multistate.MultiStateLayout
import com.tyl.common_base.R

object AppLifecycleInject : AppLifeCycles {
    override fun attachBaseContext(base: Context) {

    }

    override fun onCreate(application: Application) {
        NetManager.netConfig = NetConfig.Builder()
            .setBaseUrl("")
            .setRequestHandler(CommonNetRequestHandler())
            .build()

        Log.e("AppConfig", "onCreate: common" )

        MultiStateLayout.Builder()
            .setDefaultEmptyViewLayoutId(R.layout.common_default_empty_view)
            .setDefaultLoadErrorViewLayoutId(R.layout.common_default_load_error_view)
            .setDefaultNetworkErrorViewLayoutId(R.layout.common_default_load_error_view)
            .setDefaultLoadingViewLayoutId(R.layout.common_default_empty_view)
    }

    override fun onTerminate(application: Application) {

    }
}