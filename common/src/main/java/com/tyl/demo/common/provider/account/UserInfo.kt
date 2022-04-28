package com.tyl.demo.common.provider.account

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(
    val memberId: String = "",
    val token: String = ""
) : Parcelable

operator fun UserInfo?.plus(info: UserInfo?): UserInfo? {
    if (this == null && info == null) return null
    // TODO UserInfo 字段修改后需要在这里补充
    return UserInfo(
        memberId = info?.memberId ?: this?.memberId ?: "",
        token = info?.token ?: this?.token ?: ""
    )
}