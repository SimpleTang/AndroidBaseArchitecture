package com.tyl.base_lib.provider.impl.kvprovider

import com.tyl.base_lib.provider.ProviderManager
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 使用属性代理自动保存/获取值
 * (仿造：https://github.com/DylanCaiCoding/MMKV-KTX)
 */

interface KVOwner {
    val kvProvider: KVProvider get() = ProviderManager.get()
    val groupName: String? get() = kvProvider.getDefaultGroup()
}

fun KVOwner.kvInt(default: Int = 0) = KVProperty(KVProvider::getIntValue, KVProvider::setIntValue, default)

fun KVOwner.kvBoolean(default: Boolean = false) =
    KVProperty(KVProvider::getBooleanValue, KVProvider::setBooleanValue, default)

fun KVOwner.kvLong(default: Long = 0) =
    KVProperty(KVProvider::getLongValue, KVProvider::setLongValue, default)

fun KVOwner.kvFloat(default: Float = 0f) =
    KVProperty(KVProvider::getFloatValue, KVProvider::setFloatValue, default)

fun KVOwner.kvDouble(default: Double = 0.0) =
    KVProperty(KVProvider::getDoubleValue, KVProvider::setDoubleValue, default)

fun KVOwner.kvString(defaultValue: String? = null) =
    KVNullableProperty(KVProvider::getStringValue, KVProvider::setStringValue, defaultValue)

inline fun <reified T> kvBean() = KVBeanProperty(T::class.java)

class KVProperty<V>(
    private val decode: KVProvider.(String, V, String?) -> V,
    private val encode: KVProvider.(String, V, String?) -> Boolean,
    private val defaultValue: V
) : ReadWriteProperty<KVOwner, V> {
    override fun getValue(thisRef: KVOwner, property: KProperty<*>): V {
        return thisRef.kvProvider.decode(property.name, defaultValue, thisRef.groupName)
    }

    override fun setValue(thisRef: KVOwner, property: KProperty<*>, value: V) {
        thisRef.kvProvider.encode(property.name, value, thisRef.groupName)
    }
}

class KVNullableProperty<V>(
    private val decode: KVProvider.(String, V?, String?) -> V?,
    private val encode: KVProvider.(String, V?, String?) -> Boolean,
    private val defaultValue: V?
) : ReadWriteProperty<KVOwner, V?> {
    override fun getValue(thisRef: KVOwner, property: KProperty<*>): V? {
        return thisRef.kvProvider.decode(property.name, defaultValue, thisRef.groupName)
    }

    override fun setValue(thisRef: KVOwner, property: KProperty<*>, value: V?) {
        thisRef.kvProvider.encode(property.name, value, thisRef.groupName)
    }
}

class KVBeanProperty<V>(
    private val clazz: Class<V>,
    private val defaultValue: V? = null
) : ReadWriteProperty<KVOwner, V?> {
    override fun getValue(thisRef: KVOwner, property: KProperty<*>): V? {
        return thisRef.kvProvider.getBeanValue(
            property.name,
            clazz,
            defaultValue,
            thisRef.groupName
        )
    }

    override fun setValue(thisRef: KVOwner, property: KProperty<*>, value: V?) {
        thisRef.kvProvider.setBeanValue(property.name, defaultValue, thisRef.groupName)
    }
}