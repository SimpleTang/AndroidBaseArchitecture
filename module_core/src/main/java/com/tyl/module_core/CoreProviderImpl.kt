package com.tyl.module_core

import android.content.Context
import android.util.Log
import com.nacai.base_lib.provider.ProviderManager
import com.nacai.common.provider.CoreProvider

class CoreProviderImpl:CoreProvider {
    override fun show(msg: String) {
        Log.e("ModuleProvider", "show: CoreProviderImpl , $msg" )
    }

    override fun init(context: Context) {
        Log.e("ModuleProvider", "init: CoreProviderImpl" )
    }
}