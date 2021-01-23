package com.nacai.base_lib.integration

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.nacai.base_lib.integration.AppLifeCycles

/***
 * 框架整体的生命周期注入类
 */

interface ModuleConfig {
    /**
     * 使用[AppLifeCycles]在Application的生命周期中注入一些操作
     */
    fun injectAppLifecycle(context: Context, lifeCycles: MutableList<AppLifeCycles>)

    /**
     * 使用[Application.ActivityLifecycleCallbacks]在Activity的生命周期中注入一些操作
     */
    fun injectActivityLifecycle(context: Context, lifeCycles: MutableList<Application.ActivityLifecycleCallbacks>)


    /**
     * 使用[FragmentManager.FragmentLifecycleCallbacks]在Fragment的生命周期中注入一些操作
     */
    fun injectFragmentLifecycle(context: Context, lifeCycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>)
}