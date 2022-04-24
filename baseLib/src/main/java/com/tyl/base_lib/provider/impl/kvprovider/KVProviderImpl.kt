package com.tyl.base_lib.provider.impl.kvprovider

import android.content.Context
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import com.tyl.base_lib.base.application

internal class KVProviderImpl : KVProvider {
    private var defaultGroup: String = KVProvider.GROUP_SYS

    override fun setDefaultGroup(group: String) {
        defaultGroup = group.takeIf { it.isNotEmpty() } ?: KVProvider.GROUP_SYS
    }

    override fun getDefaultGroup(): String {
        return defaultGroup
    }

    override fun setValue(key: String, value: Any, targetGroup: String?): Boolean {
        return when (value) {
            is Boolean -> {
                setBooleanValue(key, value, targetGroup)
            }
            is String -> {
                setStringValue(key, value, targetGroup)
            }
            is Int -> {
                setIntValue(key, value, targetGroup)
            }
            is Long -> {
                setLongValue(key, value, targetGroup)
            }
            is Float -> {
                setFloatValue(key, value, targetGroup)
            }
            is Double -> {
                setDoubleValue(key, value, targetGroup)
            }
            is Unit -> {
                true
            }
            else -> {
                setBeanValue(key, value, targetGroup)
            }
        }
    }

    override fun getStringValue(key: String, default: String?, targetGroup: String?): String? {
        return MMKV.mmkvWithID(targetGroup).decodeString(key, default)
    }

    override fun setStringValue(key: String, value: String?, targetGroup: String?): Boolean {
        return MMKV.mmkvWithID(targetGroup).encode(key, value ?: "")
    }

    override fun getBooleanValue(key: String, default: Boolean, targetGroup: String?): Boolean {
        return MMKV.mmkvWithID(targetGroup).decodeBool(key, default)
    }

    override fun setBooleanValue(key: String, value: Boolean, targetGroup: String?): Boolean {
        return MMKV.mmkvWithID(targetGroup).encode(key, value)
    }

    override fun getIntValue(key: String, default: Int, targetGroup: String?): Int {
        return MMKV.mmkvWithID(targetGroup).decodeInt(key, default)
    }

    override fun setIntValue(key: String, value: Int, targetGroup: String?): Boolean {
        return MMKV.mmkvWithID(targetGroup).encode(key, value)
    }

    override fun getLongValue(key: String, default: Long, targetGroup: String?): Long {
        return MMKV.mmkvWithID(targetGroup).decodeLong(key, default)
    }

    override fun setLongValue(key: String, value: Long, targetGroup: String?): Boolean {
        return MMKV.mmkvWithID(targetGroup).encode(key, value)
    }

    override fun getFloatValue(key: String, default: Float, targetGroup: String?): Float {
        return MMKV.mmkvWithID(targetGroup).decodeFloat(key, default)
    }

    override fun setFloatValue(key: String, value: Float, targetGroup: String?): Boolean {
        return MMKV.mmkvWithID(targetGroup).encode(key, value)
    }

    override fun getDoubleValue(key: String, default: Double, targetGroup: String?): Double {
        return MMKV.mmkvWithID(targetGroup).decodeDouble(key, default)
    }

    override fun setDoubleValue(key: String, value: Double, targetGroup: String?): Boolean {
        return MMKV.mmkvWithID(targetGroup).encode(key, value)
    }

    override fun <T> getBeanValue(
        key: String,
        clazz: Class<T>,
        default: T?,
        targetGroup: String?
    ): T? {
        val str = MMKV.mmkvWithID(targetGroup).decodeString(key, null)
        if (str.isNullOrEmpty()) {
            return default
        }
        return try {
            Gson().fromJson(str, clazz)
        } catch (e: Exception) {
            default
        }
    }

    override fun <T> setBeanValue(key: String, value: T?, targetGroup: String?): Boolean {
        val str = Gson().toJson(value)
        return MMKV.mmkvWithID(targetGroup).encode(key, str)
    }

    override fun clearValue(key: String, targetGroup: String?) {
        MMKV.mmkvWithID(targetGroup).remove(key)
    }

    override fun clearAllValue(targetGroup: String?) {
        MMKV.mmkvWithID(targetGroup).clearAll()
    }

    override fun init(context: Context) {
        MMKV.initialize(application)
    }

}