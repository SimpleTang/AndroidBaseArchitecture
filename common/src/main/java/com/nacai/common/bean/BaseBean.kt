package com.nacai.common.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class BaseBean<T> constructor(
    var data: T?,
    var code: Int = 0,
    var message: String = "",
    var errorcode: Int = 0
)

class EmptyBean