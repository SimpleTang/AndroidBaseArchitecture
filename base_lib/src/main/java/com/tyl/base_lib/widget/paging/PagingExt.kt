package com.tyl.base_lib.widget.paging

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.tyl.base_lib.widget.multiplestatus.IMultipleStatusLayout
import com.tyl.base_lib.widget.multiplestatus.IMultipleStatusView
import com.tyl.base_lib.widget.multiplestatus.IStatusClickableView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener

fun <V : Any> ViewModel.pagerData(block: suspend (index: Int, size: Int) -> List<V>): LazyPager<V> {
    return LazyPager(viewModelScope, block)
}

fun getPagePrevKey(index: Int): Int? {
    return (index - 1).takeIf { it > 0 }
}

fun getPageNextKey(index: Int, loadDataSize: Int, needSize: Int): Int? {
    return (index + 1).takeIf { loadDataSize >= needSize }
}

fun <V : Any> PagingDataAdapter<V, *>.attachData(lifecycleOwner: LifecycleOwner, data: LiveData<PagingData<V>>) {
    data.observe(lifecycleOwner, Observer { pagingData ->
        lifecycleOwner.lifecycleScope.launchWhenStarted {
            submitData(pagingData)
        }
    })
}

fun PagingDataAdapter<*, *>.bindViews(refreshLayout: SmartRefreshLayout, stateView: IMultipleStatusLayout? = null) {
    refreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
        override fun onRefresh(refreshLayout: RefreshLayout) {
            refresh()
        }

        override fun onLoadMore(refreshLayout: RefreshLayout) {
            retry()
        }
    })
    var hasRefreshing = false
    var hasLoadingMore = false
    addLoadStateListener {
        // 下拉刷新
        when (it.refresh) {
            is LoadState.Error -> {
                refreshLayout.finishRefresh(false)
                // 显示下拉刷新时的 error 界面
                stateView?.status = IMultipleStatusView.Status.Error
            }
            is LoadState.Loading -> {
                hasRefreshing = true
                //如果是手动下拉刷新，则不展示loading页
                if (refreshLayout.state != RefreshState.Refreshing) {
                    // 显示下拉刷新时的 loading 界面
                    stateView?.status = IMultipleStatusView.Status.Loading
                }
            }
            is LoadState.NotLoading -> {
                if (hasRefreshing) {
                    hasRefreshing = false
                    refreshLayout.finishRefresh(true)
                    //如果第一页数据就没有更多了，第一页不会触发append
                    if (it.source.append.endOfPaginationReached) {
                        //没有更多了(只能用source的append)
                        refreshLayout.finishLoadMoreWithNoMoreData()
                    }
                    // 显示下拉刷新时的 content 界面
                    stateView?.status = IMultipleStatusView.Status.Content
                }
            }
        }

        // 向后加载更多
        when (it.append) {
            is LoadState.Error -> {
                refreshLayout.finishLoadMore(false)
            }
            is LoadState.Loading -> {
                hasLoadingMore = true
                //重置上拉加载状态，显示加载loading
                refreshLayout.resetNoMoreData()
            }
            is LoadState.NotLoading -> {
                if (hasLoadingMore) {
                    hasLoadingMore = false
                    if (it.source.append.endOfPaginationReached) {
                        //没有更多了(只能用source的append)
                        refreshLayout.finishLoadMoreWithNoMoreData()
                    } else {
                        refreshLayout.finishLoadMore(true)
                    }
                }
            }
        }
    }

    (stateView?.getViewByStatus(IMultipleStatusView.Status.Error) as? IStatusClickableView)?.setOnClickListener {
        refresh()
    }
}

fun <T : Any> diffItems(vararg contrast: (T) -> Any) =
    object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            val first = contrast.getOrNull(0)
            return if (first != null) {
                first(oldItem) == first(newItem)
            } else {
                oldItem == newItem
            }
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            for (c in contrast) {
                if (c(oldItem) != c(newItem)) {
                    return false
                }
            }
            return true
        }
    }

fun <V : Any, binding : ViewDataBinding> pageAdapter(
    @LayoutRes layoutId: Int,
    diffItemCallBack: DiffUtil.ItemCallback<V>,
    dataFactory: (() -> LiveData<PagingData<V>>)? = null,
    lifecycleOwnerFactory: () -> LifecycleOwner,
    bindBlock: (holder: BindingViewHolder<binding>, position: Int, item: V?) -> Unit
): LazyAdapter<V, binding> {
    return LazyAdapter(layoutId, diffItemCallBack, dataFactory, lifecycleOwnerFactory, bindBlock)
}