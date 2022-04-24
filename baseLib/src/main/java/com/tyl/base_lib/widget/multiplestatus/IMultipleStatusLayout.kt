package com.tyl.base_lib.widget.multiplestatus

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

interface IMultipleStatusLayout : IMultipleStatusView {

    fun getViewByStatus(status: IMultipleStatusView.Status): View?

    fun setViews(status: IMultipleStatusView.Status, view: View)

    fun <T> observerData(lifecycleOwner: LifecycleOwner, data: LiveData<T?>)

}