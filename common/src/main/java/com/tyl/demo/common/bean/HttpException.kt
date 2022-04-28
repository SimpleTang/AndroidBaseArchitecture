package com.tyl.demo.common.bean

import com.tyl.base_lib.network.ApiException
import com.tyl.demo.common.bean.BaseBean

/**
 * 业务异常 非200
 */
class HttpException(val baseBean: BaseBean<*>) : ApiException(baseBean.message, baseBean.code)