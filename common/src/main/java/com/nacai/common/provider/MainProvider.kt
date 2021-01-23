package com.nacai.common.provider

import com.nacai.base_lib.provider.ModuleProvider

interface MainProvider:ModuleProvider {
    fun show(msg:String)
}