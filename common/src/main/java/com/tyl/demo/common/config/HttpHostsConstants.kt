package com.tyl.demo.common.config

import com.tyl.demo.common.BuildConfig


object HttpHostsConstants {

    //资源目录
    // private const val RESOURCE_BASE_URL = "https://resource.maimaichat.com/"
    private const val RESOURCE_BASE_URL = "https://img.maimaichat.com/"

    /**
     * 根据不同的打包命令自动查找对应的base_url
     * 1,开发环境打包  gradlew clean assembleVersionDevDebug或者gradlew clean assembleVersionDevRelease
     * 2,测试环境打包  gradlew clean assembleVersionTestDebug或者gradlew clean assembleVersionTestRelease
     * 3,正式环境打包 gradlew clean assembleVersionOnlineRelease
     */
    /*@JvmStatic
    fun getBaseUrl(): String = if (BuildConfig.VERSION_ONLINE) RELEASE_BASE_URL else{
            if (BuildConfig.VERSION_TEST) TEST_BASE_URL  else DEV_BASE_URL
        }*/

    @JvmStatic
    fun getBaseResourceUrl(): String = BuildConfig.RES_HOST

    private const val DEFAULT_SHARE_URL = "https://wwwtest.maimaichat.com/"

    @JvmStatic
    fun getDefaultShareUrl(): String = DEFAULT_SHARE_URL
}