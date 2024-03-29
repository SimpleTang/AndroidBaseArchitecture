package com.tyl.base_lib.integration

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.tyl.base_lib.tools.ManifestParser

/***
 *
 * 关于Application/Activity/Fragment的生命周期处理类
 */

class AppDelegate(context: Context) : AppLifeCycles {
    private var mApplication: Application? = null
    private var mModuleConfigs: List<ModuleConfig>? = null
    private var mAppLifeCycles: MutableList<AppLifeCycles> = arrayListOf()
    private var mActivityLifeCycles: MutableList<Application.ActivityLifecycleCallbacks> = arrayListOf()
    private var mFragmentLifeCycles: MutableList<FragmentManager.FragmentLifecycleCallbacks> = arrayListOf()

    init {
        mModuleConfigs = ManifestParser.parseConfigModules(context)
        mModuleConfigs?.forEach {
            it.injectAppLifecycle(context, mAppLifeCycles)
            it.injectActivityLifecycle(context, mActivityLifeCycles)
            it.injectFragmentLifecycle(context, mFragmentLifeCycles)
        }
    }

    override fun attachBaseContext(base: Context) {
        mAppLifeCycles.forEach {
            it.attachBaseContext(base)
        }
    }

    override fun onCreate(application: Application) {
        mApplication = application
        mApplication?.registerActivityLifecycleCallbacks(FragmentLifecycleCallbacks())
        mActivityLifeCycles.forEach {
            mApplication?.registerActivityLifecycleCallbacks(it)
        }
        mAppLifeCycles.forEach {
            if (mApplication != null) {
                it.onCreate(mApplication!!)
            }
        }

    }

    override fun onTerminate(application: Application) {
        mActivityLifeCycles.forEach {
            mApplication?.unregisterActivityLifecycleCallbacks(it)
        }
        mAppLifeCycles.forEach {
            if (mApplication != null) {
                it.onTerminate(mApplication!!)
            }
        }
    }

    private inner class FragmentLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            if (activity is FragmentActivity) {
                mFragmentLifeCycles.forEach {
                    activity.supportFragmentManager
                            .registerFragmentLifecycleCallbacks(it, true)
                }
            }
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityStarted(activity: Activity) {
        }

        override fun onActivityDestroyed(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityStopped(activity: Activity) {
        }

    }
}