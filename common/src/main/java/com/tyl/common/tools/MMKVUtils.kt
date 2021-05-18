package com.tyl.common.tools

import com.tencent.mmkv.MMKV

object MMKVUtils {

    const val USER_TOKEN = "userToken"

    @JvmStatic
    fun getCurrentUserToken(): String? {
        return ""
    }

    @JvmStatic
    fun getCurrentUserID(): String? {
        return ""
    }

    @JvmStatic
    fun removeUserInfoByCache() {
        getCurrentUserID()?.let { userId ->
            MMKV.mmkvWithID(userId).removeValueForKey(USER_TOKEN)
        }
    }
}