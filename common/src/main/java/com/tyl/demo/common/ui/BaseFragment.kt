package com.tyl.demo.common.ui

import androidx.databinding.ViewDataBinding
import com.tyl.base_lib.base.BaseFragment

abstract class BaseFragment<B : ViewDataBinding>:BaseFragment<B>() {
    override fun setLoadingDialog(show: Boolean, msg: String) {
        (activity as? BaseActivity<*>)?.setLoadingDialog(show, msg)
    }
}