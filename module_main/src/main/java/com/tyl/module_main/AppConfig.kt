package com.tyl.module_main

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentManager
import com.tyl.base_lib.integration.AppLifeCycles
import com.tyl.base_lib.integration.ModuleConfig

class AppConfig:ModuleConfig {

    override fun injectAppLifecycle(context: Context, lifeCycles: MutableList<AppLifeCycles>) {
        lifeCycles.add(object : AppLifeCycles {
            override fun attachBaseContext(base: Context) {

            }

            override fun onCreate(application: Application) {
                Log.e("AppConfig", "onCreate: module_main" )
            }

            override fun onTerminate(application: Application) {
            }
        })
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