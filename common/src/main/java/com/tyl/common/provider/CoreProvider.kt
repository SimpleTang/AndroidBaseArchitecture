package com.tyl.common.provider

import com.tyl.base_lib.provider.ModuleProvider

interface CoreProvider:ModuleProvider {
    fun show(msg:String)
}