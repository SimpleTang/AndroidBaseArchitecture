package com.tyl.base_lib.integration

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager

/***
 * 框架整体的生命周期注入类
 * 注意: ModuleConfig 会在 Application 中被调用，为了避免 APP 启动过慢，如非必要, module 中的一些第三方库不建议在此初始化，
 * 可以在 ModuleProvider.init(ApplicationContext) 中处理
 * ModuleConfig 并不是必须的，在某些 module 中你可以不实现。
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