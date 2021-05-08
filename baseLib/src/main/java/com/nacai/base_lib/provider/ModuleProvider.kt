package com.nacai.base_lib.provider

import android.content.Context

interface ModuleProvider {
    /**
     * 注意：此方法将在首次创建的时候调用，不排除在子线程调用的可能性
     */
    fun init(context: Context)
}