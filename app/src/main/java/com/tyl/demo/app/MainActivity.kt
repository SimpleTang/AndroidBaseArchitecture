package com.tyl.demo.app

import com.tyl.base_lib.provider.providers
import com.tyl.demo.app.databinding.ActivityMainBinding
import com.tyl.demo.common.provider.main.MainProvider
import com.tyl.demo.common.ui.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_main

    private val mainProvider by providers<MainProvider>()

    override fun initView() {
        mainProvider.getSplashFragment().let {
            supportFragmentManager.beginTransaction().add(R.id.fcvContent,it, it::class.java.simpleName).commitAllowingStateLoss()
        }
    }

    override fun initData() {
    }
}