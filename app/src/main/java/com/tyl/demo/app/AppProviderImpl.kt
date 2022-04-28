package com.tyl.demo.app

import android.content.Context
import com.tyl.demo.common.provider.AppProvider
import com.tyl.process_annotation.Provider

@Provider
class AppProviderImpl : AppProvider {
    override fun getAppVersionCode(): Int {
        return BuildConfig.VERSION_CODE
    }

    override fun getAppVersionName(): String {
        return BuildConfig.VERSION_NAME
    }

    override fun init(context: Context) {
    }
}