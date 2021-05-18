package com.tyl.common.config

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.tyl.base_lib.integration.AppLifeCycles
import com.tyl.base_lib.integration.ModuleConfig

class AppConfig : ModuleConfig {
    override fun injectAppLifecycle(context: Context, lifeCycles: MutableList<AppLifeCycles>) {
        lifeCycles.add(AppLifecycleInject)
    }

    override fun injectActivityLifecycle(
        context: Context,
        lifeCycles: MutableList<Application.ActivityLifecycleCallbacks>
    ) {

    }

    override fun injectFragmentLifecycle(
        context: Context,
        lifeCycles: MutableList<FragmentManager.FragmentLifecycleCallbacks>
    ) {

    }
}