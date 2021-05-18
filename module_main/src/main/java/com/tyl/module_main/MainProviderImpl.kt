package com.tyl.module_main

import android.content.Context
import android.util.Log
import com.tyl.common.provider.MainProvider

class MainProviderImpl:MainProvider {
    override fun show(msg: String) {
        Log.e("ModuleProvider", "show: MainProviderImpl , $msg" )
//        ProviderManager.get<CoreProvider>().show(msg)
    }

    override fun init(context: Context) {
        Log.e("ModuleProvider", "init: MainProviderImpl" )
    }
}