package com.nacai.base_lib.tools

import android.content.Context
import android.content.pm.PackageManager
import com.nacai.base_lib.integration.ConfigModule
import com.nacai.base_lib.provider.ModuleProvider
import java.util.*
import kotlin.collections.HashMap

/***
 * Manifest
 * 关于在Manifest中获取ConfigModule配置处理类
 */
object ManifestParser {
    private const val MODULE_VALUE = "ConfigModule"
    private const val PROVIDER_VALUE = "ModuleProvider"

    fun parseConfigModules(context: Context): List<ConfigModule> {
        val modules = ArrayList<ConfigModule>()
        try {
            val appInfo = context.packageManager.getApplicationInfo(
                context.packageName, PackageManager.GET_META_DATA
            )
            if (appInfo.metaData != null) {
                for (key in appInfo.metaData.keySet()) {
                    if (MODULE_VALUE == appInfo.metaData.get(key)) {
                        parseModuleByName(key)?.let {
                            modules.add(it)
                        }
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
//            throw RuntimeException("Unable to find metadata to parse ConfigModule", e)
        }

        return modules
    }

    private fun parseModuleByName(className: String): ConfigModule? {
        val clazz: Class<*>
        try {
            clazz = Class.forName(className)
        } catch (e: ClassNotFoundException) {
//            throw IllegalArgumentException("Unable to find ConfigModule implementation", e)
            return null
        }
        if(!clazz.isAssignableFrom(ConfigModule::class.java)){
            return null
        }
        val module: Any
        try {
            module = clazz.newInstance()
        } catch (e: InstantiationException) {
//            throw RuntimeException("Unable to instantiate ConfigModule implementation for $clazz", e)
            return null
        } catch (e: IllegalAccessException) {
//            throw RuntimeException("Unable to instantiate ConfigModule implementation for $clazz", e)
            return null
        }

        if (module !is ConfigModule) {
//            throw RuntimeException("Expected instanceof ConfigModule, but found: $module")
            return null
        }
        return module
    }

    fun parseProviders(context: Context): Map<Class<*>, ModuleProvider> {
        val map = HashMap<Class<*>, ModuleProvider>()
        try {
            val appInfo = context.packageManager.getApplicationInfo(
                context.packageName, PackageManager.GET_META_DATA
            )
            if (appInfo.metaData != null) {
                for (key in appInfo.metaData.keySet()) {
                    if (PROVIDER_VALUE == appInfo.metaData.get(key)) {
                        parseProviderByName(key)?.let {
                            map.put(it.first, it.second)
                        }
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
//            throw RuntimeException("Unable to find metadata to parse ConfigModule", e)
        }
        return map
    }

    private fun parseProviderByName(className: String): Pair<Class<*>, ModuleProvider>? {
        val clazz: Class<*>
        try {
            clazz = Class.forName(className)
        } catch (e: ClassNotFoundException) {
//            throw IllegalArgumentException("Unable to find Provider implementation", e)
            return null
        }
        var parentProviderClass: Class<*>? = null
        clazz.interfaces.forEach {
            if (ModuleProvider::class.java.isAssignableFrom(it)) {
                parentProviderClass = it
            }
        }
        if (parentProviderClass == null) {
//            throw IllegalArgumentException("Unable to find Provider implementation")
            return null
        }

        val provider: Any
        try {
            provider = clazz.newInstance()
        } catch (e: InstantiationException) {
//            throw RuntimeException("Unable to instantiate Provider implementation for $clazz", e)
            return null
        } catch (e: IllegalAccessException) {
//            throw RuntimeException("Unable to instantiate Provider implementation for $clazz", e)
            return null
        }

        if (provider !is ModuleProvider) {
//            throw RuntimeException("Expected instanceof Provider, but found: $provider")
            return null
        }
        return Pair(parentProviderClass!!, provider)
    }
}