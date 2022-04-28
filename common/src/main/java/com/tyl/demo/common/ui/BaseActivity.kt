package com.tyl.demo.common.ui

import androidx.databinding.ViewDataBinding
import com.tyl.base_lib.base.BaseActivity

abstract class BaseActivity<B : ViewDataBinding> : BaseActivity<B>() {
    override fun setLoadingDialog(show: Boolean, msg: String) {
        // todo 默认的 loading 弹窗
    }
}