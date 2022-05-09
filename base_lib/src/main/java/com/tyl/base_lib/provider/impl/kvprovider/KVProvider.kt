package com.tyl.base_lib.provider.impl.kvprovider

import com.tyl.base_lib.provider.ModuleProvider
import com.tyl.base_lib.provider.ProviderManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface KVProvider : ModuleProvider {

    companion object{
        const val GROUP_SYS = "sysGroup"
    }

    fun setDefaultGroup(group: String)

    fun getDefaultGroup(): String?

    fun setValue(
        key: String,
        value: Any,
        targetGroup: String? = getDefaultGroup()
    ): Boolean

    fun getStringValue(
        key: String,
        default: String? = null,
        targetGroup: String? = getDefaultGroup()
    ): String?

    fun setStringValue(
        key: String,
        value: String?,
        targetGroup: String? = getDefaultGroup()
    ): Boolean

    fun getBooleanValue(
        key: String,
        default: Boolean = false,
        targetGroup: String? = getDefaultGroup()
    ): Boolean

    fun setBooleanValue(
        key: String,
        value: Boolean,
        targetGroup: String? = getDefaultGroup()
    ): Boolean

    fun getIntValue(
        key: String, default: Int = -1,
        targetGroup: String? = getDefaultGroup()
    ): Int

    fun setIntValue(
        key: String,
        value: Int,
        targetGroup: String? = getDefaultGroup()
    ): Boolean

    fun getLongValue(
        key: String, default: Long = -1,
        targetGroup: String? = getDefaultGroup()
    ): Long

    fun setLongValue(
        key: String,
        value: Long,
        targetGroup: String? = getDefaultGroup()
    ): Boolean

    fun getFloatValue(
        key: String, default: Float = -1f,
        targetGroup: String? = getDefaultGroup()
    ): Float

    fun setFloatValue(
        key: String,
        value: Float,
        targetGroup: String? = getDefaultGroup()
    ): Boolean

    fun getDoubleValue(
        key: String, default: Double = -1.0,
        targetGroup: String? = getDefaultGroup()
    ): Double

    fun setDoubleValue(
        key: String,
        value: Double,
        targetGroup: String? = getDefaultGroup()
    ): Boolean

    fun <T> getBeanValue(
        key: String, clazz: Class<T>, default: T? = null,
        targetGroup: String? = getDefaultGroup()
    ): T?

    fun <T> setBeanValue(
        key: String,
        value: T?,
        targetGroup: String? = getDefaultGroup()
    ): Boolean

    fun clearValue(
        key: String,
        targetGroup: String? = getDefaultGroup()
    )

    fun clearAllValue(targetGroup: String? = getDefaultGroup())
}
