package com.tyl.demo.common.provider

import com.tyl.base_lib.provider.ModuleProvider

interface AppProvider : ModuleProvider {
    fun getAppVersionCode(): Int

    fun getAppVersionName(): String
}