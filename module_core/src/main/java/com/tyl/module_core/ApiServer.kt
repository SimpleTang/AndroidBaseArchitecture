package com.tyl.module_core

import com.tyl.process_annotation.NetApi

interface ApiServer {
    suspend fun getConfig()
}

@NetApi
interface NewServer{
    suspend fun getNewConfig()
}