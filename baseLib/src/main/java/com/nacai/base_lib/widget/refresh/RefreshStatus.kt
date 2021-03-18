package com.nacai.base_lib.widget.refresh

enum class RefreshStatus {

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