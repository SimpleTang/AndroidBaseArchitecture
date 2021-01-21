package com.nacai.base_lib.provider

import android.app.Application
import android.content.Context
import com.nacai.base_lib.integration.AppLifeCycles
import com.nacai.base_lib.tools.ManifestParser

object ProviderManager : AppLifeCycles {
    var providers = HashMap<Class<*>, ModuleProvider>()

    override fun attachBaseContext(base: Context) {
        if (providers.isEmpty()) {
            providers.putAll(ManifestParser.parseProviders(base))
        }
    }

    override fun onCreate(application: Application) {
        providers.forEach {
            it.value.init(application)
        }
    }

    override fun onTerminate(application: Application) {
        providers.clear()
    }

    inline fun <reified T> get(): T? {
        return providers[T::class.java] as? T
    }
}