package com.tyl.base_lib.provider

import android.app.Application
import android.content.Context
<<<<<<< HEAD
import com.tyl.base_lib.base.BaseApplication
import com.tyl.base_lib.integration.AppLifeCycles
=======
import com.tyl.base_lib.base.application
import com.tyl.base_lib.integration.AppLifeCycles
import com.tyl.base_lib.provider.impl.kvprovider.KVProvider
import com.tyl.base_lib.provider.impl.kvprovider.KVProviderImpl
>>>>>>> v0.0.1
import com.tyl.base_lib.tools.ManifestParser
import java.lang.Exception

object ProviderManager : AppLifeCycles {
    var providerClassMap = HashMap<Class<*>, Class<*>>()
    var providers = HashMap<Class<*>, ModuleProvider>()

    override fun attachBaseContext(base: Context) {
        if (providerClassMap.isEmpty()) {
<<<<<<< HEAD
            providerClassMap.putAll(ManifestParser.parseProviders(base))
=======
            ProviderRegisterManager.loadRegister()
            if(ProviderRegisterManager.isInitByPlugin){
                providerClassMap.putAll(ProviderRegisterManager.getClassMap())
            }else{
                providerClassMap.putAll(ManifestParser.parseProviders(base))
            }
            // 默认的实现
            providerClassMap[KVProvider::class.java] = KVProviderImpl::class.java
>>>>>>> v0.0.1
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
<<<<<<< HEAD
            ?: throw NullPointerException("not found provider impl : ${T::class.java.simpleName}")
=======
            ?: throw NullPointerException("not found provider impl : ${T::class.java.simpleName} , please check @Provider")
>>>>>>> v0.0.1
    }

    inline fun <reified T : ModuleProvider> getOrNull(): T? {
        synchronized(this) {
            var provider = providers[T::class.java] as? T
            if (provider == null) {
                providerClassMap[T::class.java]?.let {
                    provider = try {
<<<<<<< HEAD
                        (it.newInstance() as? T)?.apply {
                            providers[T::class.java] = this
                            init(BaseApplication.instance.applicationContext)
                        }
                    } catch (e: InstantiationException) {
                        null
                    } catch (e: IllegalAccessException) {
                        null
                    } catch (e: Exception) {
=======
                        (it.newInstance() as T).apply {
                            providers[T::class.java] = this
                            init(application.applicationContext)
                        }
                    } catch (e: InstantiationException) {
                        e.printStackTrace()
                        null
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                        null
                    } catch (e: Exception) {
                        e.printStackTrace()
>>>>>>> v0.0.1
                        null
                    }
                }
            }
            return provider
        }
    }
<<<<<<< HEAD
=======

    @Suppress("UNCHECKED_CAST")
    internal fun <T : ModuleProvider> getByClass(clazz: Class<T>): T? {
        synchronized(this) {
            var provider = providers[clazz] as? T
            if (provider == null) {
                providerClassMap[clazz]?.let {
                    provider = try {
                        (it.newInstance() as T).apply {
                            providers[clazz] = this
                            init(application)
                        }
                    } catch (e: InstantiationException) {
                        e.printStackTrace()
                        null
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                        null
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                }
            }
            return provider
        }
    }

}

inline fun <reified T : ModuleProvider> providers(): Lazy<T> {
    return ModuleProviderLazy(T::class.java)
}

class ModuleProviderLazy<T : ModuleProvider>(private val clazz: Class<T>) : Lazy<T> {
    private var cached: T? = null

    override val value: T
        get() = cached ?: (ProviderManager.getByClass(clazz)?.apply { cached = this }
            ?: throw NullPointerException("not found provider impl : ${clazz.simpleName} , please check @Provider"))

    override fun isInitialized(): Boolean {
        return cached != null
    }

>>>>>>> v0.0.1
}