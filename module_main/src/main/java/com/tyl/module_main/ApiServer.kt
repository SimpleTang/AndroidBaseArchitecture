package com.tyl.module_main

import com.tyl.process_annotation.NetApi

@NetApi(host = "https://www.baidu.com")
interface ApiServer {
    suspend fun getAConfig()
}