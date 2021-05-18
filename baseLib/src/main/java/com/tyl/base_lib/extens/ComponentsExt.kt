package com.tyl.base_lib.extens

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tyl.base_lib.base.BaseApplication


fun Activity.toActivity(activityClass: Class<*>, extras: Bundle? = null, requestCode: Int = 1991) {
    val intent = Intent(this, activityClass)
    extras?.let {
        intent.putExtras(it)
    }
    startActivityForResult(intent, requestCode)
}

fun Fragment.toActivity(activityClass: Class<*>, extras: Bundle? = null, requestCode: Int = 1991) {
    activity?.let {
        val intent = Intent(it, activityClass)
        extras?.let { b ->
            intent.putExtras(b)
        }
        startActivityForResult(intent, requestCode)
    }
}

val Float.dp: Int
    get() {
//        return ConvertUtils.dp2px(this)
        val scale = BaseApplication.instance.resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

val Int.dp: Int
    get() {
//        return ConvertUtils.dp2px(toFloat())
        val scale = BaseApplication.instance.resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

val Float.sp: Int
    get() {
//        return ConvertUtils.sp2px(this)
        val fontScale = BaseApplication.instance.resources.displayMetrics.scaledDensity
        return (this * fontScale + 0.5f).toInt()
    }

val Int.sp: Int
    get() {
//        return ConvertUtils.sp2px(toFloat())
        val fontScale = BaseApplication.instance.resources.displayMetrics.scaledDensity
        return (this * fontScale + 0.5f).toInt()
    }

