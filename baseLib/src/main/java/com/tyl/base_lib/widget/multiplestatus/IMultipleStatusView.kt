package com.tyl.base_lib.widget.multiplestatus

interface IMultipleStatusView {

    var status: Status

    enum class Status {
        Init,
        Loading,
        Empty,
        Error,
        Content
    }
}

