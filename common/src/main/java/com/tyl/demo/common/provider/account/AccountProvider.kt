package com.tyl.demo.common.provider.account

import android.app.Activity
import android.content.Intent
import com.tyl.base_lib.provider.ModuleProvider
import com.tyl.base_lib.tools.ActivityStack

interface AccountProvider : ModuleProvider {

    fun fastLogin(success: (UserInfo) -> Unit = {}, fail: () -> Unit = {})

    fun toLoginPage(aty: Activity? = ActivityStack.getTopActivity(), ext: Intent? = null)

    fun logout(success: () -> Unit = {})
}