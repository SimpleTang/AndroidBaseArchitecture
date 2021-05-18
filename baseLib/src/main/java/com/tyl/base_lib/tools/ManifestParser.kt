package com.tyl.base_lib.tools

import android.content.Context
import android.content.pm.PackageManager
import com.tyl.base_lib.integration.ModuleConfig
import com.tyl.base_lib.provider.ModuleProvider
import java.util.*
import kotlin.collections.HashMap

/***
 * Manifest
 * 关于在Manifest中获取ConfigModule配置处理类
 */
object ManifestParser {
    private const val MODULE_VALUE = "ModuleConfig"
    private const val PROVIDER_VALUE = "ModuleProvider"

    fun parseConfigModules(context: Context): List<ModuleConfig> {
        val modules = ArrayList<ModuleConfig>()
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

    private fun parseModuleByName(className: String): ModuleConfig? {
        val clazz: Class<*>
        try {
            clazz = Class.forName(className)
        } catch (e: ClassNotFoundException) {
//            throw IllegalArgumentException("Unable to find ConfigModule implementation", e)
            return null
        }
        if (!ModuleConfig::class.java.isAssignableFrom(clazz)) {
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

        if (module !is ModuleConfig) {
//            throw RuntimeException("Expected instanceof ConfigModule, but found: $module")
            return null
        }
        return module
    }

    fun parseProviders(context: Context): Map<Class<*>, Class<*>> {
        val map = HashMap<Class<*>, Class<*>>()
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
        }
        return map
    }

    private fun parseProviderByName(className: String): Pair<Class<*>, Class<*>>? {
        val clazz: Class<*>
        try {
            clazz = Class.forName(className)
        } catch (e: ClassNotFoundException) {
            return null
        }
        var parentProviderClass: Class<*>? = null
        clazz.interfaces.forEach {
            if (ModuleProvider::class.java.isAssignableFrom(it)) {
                parentProviderClass = it
            }
        }
        if (parentProviderClass == null) {
            return null
        }
        // 延迟实例化，用到的时候再加载
        /*val provider: Any
        try {
            provider = clazz.newInstance()
        } catch (e: InstantiationException) {
            return null
        } catch (e: IllegalAccessException) {
            return null
        }

        if (provider !is ModuleProvider) {
            return null
        }*/
        return Pair(parentProviderClass!!, clazz)
    }
}