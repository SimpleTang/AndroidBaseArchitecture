package com.tyl.demo.common.tools

import com.tyl.demo.common.provider.AppProvider
import com.tyl.demo.common.provider.account.UserInfo
import com.tyl.demo.common.provider.storage.DataSourceProvider
import com.tyl.base_lib.provider.ProviderManager
import com.tyl.base_lib.provider.impl.kvprovider.KVProvider
import com.tyl.base_lib.provider.providers

object MMKVUtils {

    private val kvProvider by providers<KVProvider>()

    private val dsProvider = ProviderManager.get<DataSourceProvider>()

    private val appProvider by providers<AppProvider>()

    /**
     *
     */
    @Deprecated("推荐使用 DataSourceProvider")
    @JvmStatic
    fun setCurrentUserInfo(userInfo: UserInfo?, cover: Boolean = false) {
        if (cover) {
            dsProvider.setCurrentUserInfo(userInfo)
        } else {
            dsProvider.updateCurrentUserInfo(userInfo)
        }
    }

    @Deprecated("推荐使用 DataSourceProvider")
    @JvmStatic
    fun getCurrentUserInfo(): UserInfo? {
        return dsProvider.currentUserInfo.value
    }

    @Deprecated("推荐使用 DataSourceProvider")
    @JvmStatic
    fun getCurrentUserID(): String? {
        return dsProvider.getCurrentUserId()
    }

    @Deprecated("推荐使用 DataSourceProvider")
    @JvmStatic
    fun getCurrentUserToken(): String? {
        return dsProvider.getCurrentUserToken()
    }

    @Deprecated("推荐使用 DataSourceProvider")
    @JvmStatic
    fun removeUserInfoByCache() {
        dsProvider.clearCurrentUserInfo()
    }


    // ------------------------------------------------------------------------ //

    private const val HAS_AGREE_PRIVACY = "HAS_AGREE_PRIVACY"
    private const val APP_SOURCE = "APP_SOURCE"
    private const val DEVICE_OAID = "DEVICE_IDFA"
    const val DEVICE_KEY_UNKNOWN = "unknown"

    //设置是否同意隐私协议
    @JvmStatic
    fun setHasAgreePrivacy(has: Boolean) {
        kvProvider.setValue(HAS_AGREE_PRIVACY, has)
    }

    //获取是否同意隐私协议
    @JvmStatic
    fun getHasAgreePrivacy(): Boolean {
        return kvProvider.getBooleanValue(HAS_AGREE_PRIVACY, false)
    }

    //设置渠道
    @JvmStatic
    fun setAppSource(appSource: String) {
        kvProvider.setValue(APP_SOURCE, appSource)
    }

    //获取渠道
    @JvmStatic
    fun getAppSource(): String {
        return kvProvider.getStringValue(APP_SOURCE) ?: "defaultSource"
    }

    //设置设备OAID
    @JvmStatic
    fun setDeviceOAID(appSource: String) {
        kvProvider.setValue(DEVICE_OAID, appSource)
    }

    //获取设备OAID
    @JvmStatic
    fun getDeviceOAID(): String {
        return kvProvider.getStringValue(DEVICE_OAID) ?: DEVICE_KEY_UNKNOWN
    }

    @JvmStatic
    fun getDeviceIDWithNotNull(): String {
        return getDeviceOAID().takeIf { it.isNotEmpty() && it != DEVICE_KEY_UNKNOWN } ?: Utils.getUniquePsuedoID()
    }

    @JvmStatic
    fun getVersionCode(): Int {
        return appProvider.getAppVersionCode()
    }

    @JvmStatic
    fun getVersionName(): String {
        return appProvider.getAppVersionName()
    }
}