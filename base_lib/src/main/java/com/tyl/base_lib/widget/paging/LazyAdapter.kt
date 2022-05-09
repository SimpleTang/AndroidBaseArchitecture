package com.tyl.base_lib.widget.paging

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil

class LazyAdapter<T : Any, binding : ViewDataBinding>(
    @LayoutRes private val layoutId: Int,
    private val diffItemCallBack: DiffUtil.ItemCallback<T>,
    private val dataFactory: (() -> LiveData<PagingData<T>>)? = null,
    private val lifecycleOwnerFactory: ()->LifecycleOwner,
    private val bindBlock:(holder: BindingViewHolder<binding>, position: Int, item: T?)->Unit
) : Lazy<PagingDataBindingAdapter<T, binding>> {

    private var cached: PagingDataBindingAdapter<T, binding>? = null

    override val value: PagingDataBindingAdapter<T, binding>
        get() = object : PagingDataBindingAdapter<T, binding>(lifecycleOwnerFactory(), layoutId, diffItemCallBack, dataFactory?.invoke()) {
            override fun onBind(holder: BindingViewHolder<binding>, position: Int, item: T?) {
                bindBlock(holder, position, item)
            }
        }

    override fun isInitialized(): Boolean {
        return cached != null
    }
}