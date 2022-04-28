package com.tyl.demo.common.provider.storage

import androidx.lifecycle.LiveData
import com.tyl.demo.common.bean.ConfigRES
import com.tyl.demo.common.provider.account.UserInfo
import com.tyl.base_lib.provider.ModuleProvider

interface DataSourceProvider : ModuleProvider {

    fun initData()

    val currentUserInfo: LiveData<UserInfo?>

    fun setCurrentUserInfo(userInfo: UserInfo?)

    fun updateCurrentUserInfo(userInfo: UserInfo?)

    fun getCurrentUserId(): String?

    fun getCurrentUserToken(): String?

    fun clearCurrentUserInfo()

    fun save(key: String, value: Any?)

    fun <T> get(key: String): T?

    fun remove(key: String): Any?

    fun getAllConfig(): ConfigRES?

    fun getAllConfigWithNotNull(success: (ConfigRES) -> Unit = {})

    fun <T> getConfigByType(type: String):T?

    fun <T> getConfigByTypeWithNotNull(type: String, success: (T?) -> Unit = {})
}