package com.nacai.base_lib.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    val events = HashMap<String, MutableLiveData<VMEvent<*>>>()
    val closePageEvent: MutableLiveData<VMEvent<*>> = MutableLiveData()
    val loadingEvent: MutableLiveData<VMEvent<*>> = MutableLiveData()
    val pageStatusEvent: MutableLiveData<VMEvent<PageStatus>> = MutableLiveData()
    val refreshStatusEvent: MutableLiveData<VMEvent<RefreshStatus>> = MutableLiveData()

    init {
    }

    companion object {
//        const val EVENT_KEY_CLOSE_PAGE = "closePage"
//        const val EVENT_KEY_LOADING = "loading"
//        const val EVENT_PAGE_STATUS = "pageStatus"
//        const val EVENT_REFRESH_STATUS = "refreshStatus"
    }
}

enum class PageStatus {
    //无网络
    NO_NETWORK,

    //加载中
    LOADING,

    //暂无数据
    EMPTY,

    //加载失败
    ERROR,

    //初始化状态
    NORMAL,

    //显示内容布局
    CONTENT,
}

enum class RefreshStatus {
    //空数据
    EMPTY,

    //下拉刷新结束
    REFRESH_SUCCESS,

    //加载更多结束
    LOAD_MORE_SUCCESS,

    //下拉刷新失败
    REFRESH_FAIL,

    //加载更多失败
    LOAD_MORE_FAIL,

    //没有更多数据
    NOT_MORE,

    //初始化状态
    NORMA,
}