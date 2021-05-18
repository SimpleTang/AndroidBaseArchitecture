package com.tyl.common.provider

import com.tyl.base_lib.provider.ModuleProvider

interface MainProvider:ModuleProvider {
    fun show(msg:String)
}