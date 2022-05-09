package com.tyl.base_lib.extens

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter(value = ["showOrGone"])
fun bindVisibilityByShowOrGone(view: View, show: Boolean?) {
    view.visibility = if (show == true) View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["showOrInvisible"])
fun bindVisibilityByShowOrInvisible(view: View,show: Boolean?){
    view.visibility = if (show == true) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("onClick")
fun onClick(view: View,call:((View)->Unit)?=null){
    call?.let {
        view.onClick(call = it)
    }
}

@BindingAdapter("onClickNoAnim")
fun onClickNoAnim(view: View,call:((View)->Unit)?=null){
    call?.let {
        view.onClickNoAnim(call = it)
    }
}

//@BindingAdapter("app:itemClickPresenter","app:itemPosition","app:itemBean",requireAll = true)
//fun <T> onItemClick(view: View, call: ItemClickPresenter<T>?=null, position:Int?=null, bean:T?=null){
//    if(call!=null && position!=null && bean!=null){
//        view.onClick {
//            call.onItemClick(it,position,bean)
//        }
//    }
//}
