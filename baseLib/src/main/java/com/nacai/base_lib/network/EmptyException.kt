package com.nacai.base_lib.network

import com.blankj.utilcode.util.StringUtils
import com.nacai.base_lib.R
import java.io.IOException

/***
 * 暂无数据异常类
 */
class EmptyException(message: String = StringUtils.getString(R.string.base_empty_data)) : IOException()