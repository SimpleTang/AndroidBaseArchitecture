package com.tyl.module_core

import com.nacai.base_lib.base.BaseViewModel

class BaViewModel:BaseViewModel() {
    fun getConfig(){
        api {
            getNewConfig()
        }
    }
}