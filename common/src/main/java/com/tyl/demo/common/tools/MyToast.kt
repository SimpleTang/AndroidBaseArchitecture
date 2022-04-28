package com.tyl.demo.common.tools

import android.widget.Toast
import com.tyl.base_lib.base.BaseApplication
import com.tyl.base_lib.base.application

object MyToast {
    @JvmStatic
    fun showToast(msg: String) {
        Toast.makeText(application, msg, Toast.LENGTH_SHORT).show()
    }
}