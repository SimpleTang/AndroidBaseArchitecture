package com.tyl.base_lib.extens

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowManager

/**
 * 设置全屏
 */
fun Activity.fullscreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val lp = window.attributes
        lp.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        window.attributes = lp
    }
    window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

    var uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or  //布局位于状态栏下方
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or  //全屏
            View.SYSTEM_UI_FLAG_FULLSCREEN or  //隐藏导航栏
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    uiOptions = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    window.decorView.systemUiVisibility = uiOptions
    window.decorView
        .setOnSystemUiVisibilityChangeListener {
            uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or  //布局位于状态栏下方
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or  //全屏
                    View.SYSTEM_UI_FLAG_FULLSCREEN or  //隐藏导航栏
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            window.decorView.systemUiVisibility = uiOptions
        }
}