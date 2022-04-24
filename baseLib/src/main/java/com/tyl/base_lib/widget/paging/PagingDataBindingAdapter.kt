package com.tyl.base_lib.widget.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.tyl.base_lib.extens.onClick

abstract class PagingDataBindingAdapter<T : Any, binding : ViewDataBinding>(
    private val lifecycleOwner: LifecycleOwner,
    @LayoutRes private val layoutId: Int,
    diffItemCallBack: DiffUtil.ItemCallback<T>,
    data: LiveData<PagingData<T>>? = null
) :
    PagingDataAdapter<T, BindingViewHolder<binding>>(diffItemCallBack) {

    init {
        data?.let {
            attachData(lifecycleOwner,it)
        }
    }

    override fun onBindViewHolder(holder: BindingViewHolder<binding>, position: Int) {
        val item = getItem(position)
        holder.binding.root.onClick {
            onItemClickListener?.invoke(it, position, item)
        }
        onBind(holder, position, item)
        holder.binding.executePendingBindings()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<binding> {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        val binding = DataBindingUtil.bind<binding>(view)!!
        binding.lifecycleOwner = lifecycleOwner
        return BindingViewHolder(binding)
    }

    abstract fun onBind(holder: BindingViewHolder<binding>, position: Int, item: T?)

    private var onItemClickListener: OnItemClickListener<T>? = null

    fun setOnItemClickListener(listener: OnItemClickListener<T>){
        onItemClickListener = listener
    }
}

typealias OnItemClickListener<T> = ((view: View, position: Int, item: T?) -> Unit)