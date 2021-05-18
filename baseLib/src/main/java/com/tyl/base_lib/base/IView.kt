package com.tyl.base_lib.base

import androidx.annotation.LayoutRes

interface IView {

    @get:LayoutRes
    val layoutId: Int

    fun initView()

    fun initData()

    fun showLoading(show: Boolean, msg: String = "请稍后...")

    fun attachBaseVMEvent(viewModel: BaseViewModel)
}