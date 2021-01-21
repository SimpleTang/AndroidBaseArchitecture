package com.nacai.base_lib.extens

import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.blankj.utilcode.util.ClickUtils
import com.chad.library.adapter.base.BaseQuickAdapter

fun View.onClick(duration:Long=500, call: (View)->Unit){
    setTouchAnim()
    onClickNoAnim(duration,call)
}

fun View.onClickNoAnim(duration:Long=500, call: (View)->Unit){
    setOnClickListener(object: ClickUtils.OnDebouncingClickListener(true,duration){
        override fun onDebouncingClick(v: View?) {
            call(this@onClickNoAnim)
        }
    })
}

fun View.setTouchAnim(){
    ClickUtils.applyPressedViewScale(this)
    ClickUtils.applyPressedViewAlpha(this)
}


fun <T> BaseQuickAdapter<T, *>.bindList(list: ObservableArrayList<T>){
    list.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableArrayList<T>>() {
        override fun onChanged(sender: ObservableArrayList<T>?) {
            notifyDataSetChanged()
        }

        override fun onItemRangeChanged(sender: ObservableArrayList<T>?, positionStart: Int, itemCount: Int) {
            notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeInserted(sender: ObservableArrayList<T>?, positionStart: Int, itemCount: Int) {
            notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeMoved(sender: ObservableArrayList<T>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
            notifyItemMoved(fromPosition, toPosition)
        }

        override fun onItemRangeRemoved(sender: ObservableArrayList<T>?, positionStart: Int, itemCount: Int) {
            notifyItemRangeRemoved(positionStart, itemCount)
        }

    })
}