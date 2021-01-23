package com.tyl.module_main

import com.nacai.base_lib.base.BaseViewModel

class MViewModel:BaseViewModel() {
    fun getAConfig(){
        api {
            this.getAConfig()
        }
    }
}