package com.nacai.base_lib.widget.multistate

enum class PageStatus(val level: Int) {
    //初始化状态
    NORMAL(0),

    //加载中
    LOADING(1),

    //无网络
    NO_NETWORK(1),

    //加载失败
    ERROR(1),

    //暂无数据
    EMPTY(2),

    //显示内容布局
    CONTENT(2),
}