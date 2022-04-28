package com.tyl.demo.common.ext

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tyl.demo.common.R

inline fun <reified T : DialogFragment> FragmentManager.showDialogWithNotExist(
    dialog: T,
    onClash: (T) -> Unit = {}
) {
    val old = findFragmentByTag(dialog::class.java.simpleName)
    if (old != null && old is T) {
        onClash(old)
        return
    }
    beginTransaction()
        // 弹窗动画
        .setCustomAnimations(R.anim.anim_translate_bottom_in, R.anim.anim_translate_bottom_out)
        .add(dialog, dialog::class.java.simpleName)
        .commitAllowingStateLoss()
}

inline fun <reified T : DialogFragment> FragmentManager.showDialog(
    dialog: T
) {
    beginTransaction()
        // 弹窗动画
        .setCustomAnimations(R.anim.anim_translate_bottom_in, R.anim.anim_translate_bottom_out)
        .add(dialog, dialog::class.java.simpleName)
        .commitAllowingStateLoss()
}

fun DialogFragment.reShowDialog() {
    if (isAdded) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.anim_translate_bottom_in, R.anim.anim_translate_bottom_out)
            .hide(this)
            .commitAllowingStateLoss()
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.anim_translate_bottom_in, R.anim.anim_translate_bottom_out)
            .show(this)
            .commitAllowingStateLoss()
    }
}