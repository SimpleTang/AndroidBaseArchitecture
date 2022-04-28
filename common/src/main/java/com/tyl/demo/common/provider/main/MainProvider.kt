package com.tyl.demo.common.provider.main

import androidx.fragment.app.Fragment
import com.tyl.base_lib.provider.ModuleProvider

interface MainProvider : ModuleProvider {
    fun getSplashFragment(): Fragment
}