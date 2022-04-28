package com.tyl.demo.module_main.provider

import android.content.Context
import androidx.fragment.app.Fragment
import com.tyl.demo.common.provider.main.MainProvider
import com.tyl.demo.module_main.fragment.SplashFragment
import com.tyl.process_annotation.Provider

@Provider
class MainProviderImpl: MainProvider {
    override fun getSplashFragment(): Fragment {
        return SplashFragment()
    }

    override fun init(context: Context) {

    }
}