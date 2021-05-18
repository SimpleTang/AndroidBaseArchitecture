package com.tyl.common.bean

import com.tyl.base_lib.network.ApiException

/**
 * 业务异常 非200
 */
class HttpException(val baseBean: BaseBean<*>) : ApiException(baseBean.message)