package com.tyl.demo.module_main.fragment

import com.tyl.demo.common.tools.GuideUtils
import com.tyl.demo.common.tools.MyToast
import com.tyl.demo.common.ui.BaseFragment
import com.tyl.demo.module_main.R
import com.tyl.demo.module_main.databinding.MainFragmentSplashBinding

class SplashFragment : BaseFragment<MainFragmentSplashBinding>() {
    override val layoutId: Int
        get() = R.layout.main_fragment_splash

    override fun initView() {
    }

    override fun initData() {
        if (GuideUtils.isFirstIn) {
            MyToast.showToast("123")
        }
        GuideUtils.isFirstIn = !GuideUtils.isFirstIn
    }
}