package com.tyl.demo.module_storage.provider

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tyl.base_lib.provider.impl.kvprovider.KVProvider
import com.tyl.base_lib.provider.providers
import com.tyl.demo.common.bean.ConfigRES
import com.tyl.demo.common.provider.storage.DataSourceProvider
import com.tyl.demo.common.provider.account.UserInfo
import com.tyl.demo.common.provider.account.plus
import com.tyl.demo.module_storage.api.leakApi
import com.tyl.process_annotation.Provider
import kotlin.reflect.full.declaredMemberProperties

@Provider
class DataSourceProviderImpl : DataSourceProvider {
    companion object {
        private const val KEY_USER_TOKEN = "userToken"
        private const val KEY_USER_ID = "userId"

        private const val KEY_USER_INFO = "userInfo"

    }

    private val kvProvider by providers<KVProvider>()

    private val mUserInfo = MutableLiveData<UserInfo?>()

    override fun initData() {

    }

    override val currentUserInfo: LiveData<UserInfo?>
        get() = mUserInfo

    override fun setCurrentUserInfo(userInfo: UserInfo?) {
        mUserInfo.value = userInfo
        if (userInfo == null) {
            kvProvider.setDefaultGroup(KVProvider.GROUP_SYS)
            kvProvider.clearValue(KEY_USER_INFO)
        } else {
            kvProvider.setDefaultGroup(userInfo.memberId)
            kvProvider.setValue(KEY_USER_INFO, userInfo)
        }
    }

    override fun updateCurrentUserInfo(userInfo: UserInfo?) {
        userInfo?.let {
            val oldUserInfo = kvProvider.getBeanValue(KEY_USER_INFO, UserInfo::class.java)
            if (oldUserInfo == null || oldUserInfo.memberId == it.memberId) {
                val newInfo = oldUserInfo + it
                newInfo?.let {
                    kvProvider.setValue(KEY_USER_INFO, newInfo)
                    mUserInfo.value = newInfo
                }
            }
        }
    }

    override fun getCurrentUserId(): String? {
        return mUserInfo.value?.memberId
    }

    override fun getCurrentUserToken(): String? {
        return mUserInfo.value?.token
    }

    override fun clearCurrentUserInfo() {
        kvProvider.clearValue(KEY_USER_INFO)
    }

    private val cachedValue = HashMap<String, Any?>()

    override fun save(key: String, value: Any?) {
        cachedValue[key] = value
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(key: String): T? {
        return cachedValue[key] as? T
    }

    override fun remove(key: String): Any? {
        return cachedValue.remove(key)
    }

    private val allConfigKey = "allConfig"
    override fun getAllConfig(): ConfigRES? {
        return get(allConfigKey) ?: let {
            getAllConfigWithNotNull()
            null
        }
    }

    override fun getAllConfigWithNotNull(success: (ConfigRES) -> Unit) {
        leakApi {
            get<ConfigRES>(allConfigKey)?.apply(success)
                ?: getConfigByTypes(Gson().toJson(ConfigRES.ConfigTypes.ALL_CONFIG)).data?.let {
                    save(allConfigKey, it)
                    ConfigRES::class.declaredMemberProperties.forEach { field ->
                        field.get(it)?.let { value ->
                            save(field.name, value)
                        }
                    }
                    success(it)
                }
        }
    }

    override fun <T> getConfigByType(type: String): T? {
        return get(type) ?: let {
            getConfigByTypeWithNotNull<T>(type)
            null
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getConfigByTypeWithNotNull(type: String, success: (T?) -> Unit) {
        leakApi {
            get<T>(type)?.apply(success)
                ?: getConfigByTypes(Gson().toJson(arrayListOf(type))).data?.let { res ->
                    ConfigRES::class.declaredMemberProperties.find { it.name == type }?.let {
                        (it.get(res) as? T).let { value ->
                            success(value)
                            save(it.name, value)
                        }
                    }
                }
        }
    }

    override fun init(context: Context) {
        val currentUserInfo = kvProvider.getBeanValue(KEY_USER_INFO, UserInfo::class.java)
        mUserInfo.postValue(currentUserInfo)
        kvProvider.setDefaultGroup(currentUserInfo?.memberId ?: KVProvider.GROUP_SYS)
    }


}