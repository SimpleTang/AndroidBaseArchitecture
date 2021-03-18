package com.nacai.common.ext

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.nacai.base_lib.widget.multistate.PageStatus
import com.nacai.base_lib.extens.onClick
import com.nacai.base_lib.widget.refresh.RefreshPresenter
import com.nacai.base_lib.widget.multistate.MultiStateLayout
import com.nacai.common_base.R

@BindingAdapter(
    value = ["bindStatus_status", "bindStatus_onTryRefresh", "bindStatus_loadErrorImg", "bindStatus_loadErrorHint"],
    requireAll = false
)
fun bindStatus(
    layout: MultiStateLayout,
    status: PageStatus?,
    refreshPresenter: RefreshPresenter?,
    errorImg: Drawable?,
    errorHint: String?
) {
    layout.viewInitCallBack = object : MultiStateLayout.OnViewInitCallBack {
        override fun onAttach(view: View, state: PageStatus) {
            if (state == PageStatus.ERROR) {
                //重试按钮
                view.findViewById<View>(R.id.tvStatusViewRetryRefresh)?.onClick {
                    refreshPresenter?.loadData(true)
                }
                errorImg?.let {
                    view.findViewById<ImageView>(R.id.ivStatusViewLoadErrorImg)
                        ?.setImageDrawable(it)
                }
                errorHint?.let {
                    view.findViewById<TextView>(R.id.tvStatusViewLoadErrorHint)?.text = it
                }
            }
        }
    }
    status?.let {
        layout.status = it
    }
}