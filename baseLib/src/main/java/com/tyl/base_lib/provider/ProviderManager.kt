package com.tyl.base_lib.provider

import android.app.Application
import android.content.Context
import com.tyl.base_lib.base.BaseApplication
import com.tyl.base_lib.integration.AppLifeCycles
import com.tyl.base_lib.tools.ManifestParser
import java.lang.Exception

object ProviderManager : AppLifeCycles {
    var providerClassMap = HashMap<Class<*>, Class<*>>()
    var providers = HashMap<Class<*>, ModuleProvider>()

    override fun attachBaseContext(base: Context) {
        if (providerClassMap.isEmpty()) {
            providerClassMap.putAll(ManifestParser.parseProviders(base))
        }
    }

    override fun onCreate(application: Application) {
        // 延迟初始化
        /*providers.forEach {
            it.value.init(application)
        }*/
    }

    override fun onTerminate(application: Application) {
        providers.clear()
        providerClassMap.clear()
    }

    inline fun <reified T : ModuleProvider> get(): T {
        return getOrNull()
            ?: throw NullPointerException("not found provider impl : ${T::class.java.simpleName}")
    }

    inline fun <reified T : ModuleProvider> getOrNull(): T? {
        synchronized(this) {
            var provider = providers[T::class.java] as? T
            if (provider == null) {
                providerClassMap[T::class.java]?.let {
                    provider = try {
                        (it.newInstance() as? T)?.apply {
                            providers[T::class.java] = this
                            init(BaseApplication.instance.applicationContext)
                        }
                    } catch (e: InstantiationException) {
                        null
                    } catch (e: IllegalAccessException) {
                        null
                    } catch (e: Exception) {
                        null
                    }
                }
            }
            return provider
        }
    }
}