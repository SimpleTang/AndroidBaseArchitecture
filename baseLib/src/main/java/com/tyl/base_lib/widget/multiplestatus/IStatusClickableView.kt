package com.tyl.base_lib.widget.multiplestatus

import android.view.View

interface IStatusClickableView {
    fun setOnClickListener(onClick: ((View) -> Unit)?)
}