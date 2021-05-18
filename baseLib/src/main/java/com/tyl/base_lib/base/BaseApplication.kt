package com.tyl.base_lib.base

import android.app.Application
import android.content.Context
import com.tyl.base_lib.integration.AppDelegate
import com.tyl.base_lib.provider.ProviderManager

open class BaseApplication : Application() {
    companion object {
        lateinit var instance: BaseApplication
            private set
    }

    private var mAppDelegate: AppDelegate? = null

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        if (mAppDelegate == null) {
            mAppDelegate = AppDelegate(base)
        }
        mAppDelegate?.attachBaseContext(base)
        ProviderManager.attachBaseContext(base)
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        mAppDelegate?.onCreate(this)
        ProviderManager.onCreate(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        mAppDelegate?.onTerminate(this)
        ProviderManager.onTerminate(this)
    }
}