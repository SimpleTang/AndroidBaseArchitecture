package com.tyl.module_main

import com.tyl.base_lib.base.BaseViewModel

class MViewModel:BaseViewModel() {
    fun getAConfig(){
        api {
            this.getAConfig()
        }
    }
}