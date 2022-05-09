package com.tyl.base_lib.widget.paging

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BindingViewHolder<out T: ViewDataBinding>(val binding:T): RecyclerView.ViewHolder(binding.root)