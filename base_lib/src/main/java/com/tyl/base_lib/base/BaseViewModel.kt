package com.tyl.base_lib.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    val events = HashMap<String, MutableLiveData<VMEvent<*>>>()
    val closePageEvent: MutableLiveData<VMEvent<*>> = MutableLiveData()
    val loadingEvent: MutableLiveData<VMEvent<*>> = MutableLiveData()

    init {
    }

    companion object {
//        const val EVENT_KEY_CLOSE_PAGE = "closePage"
//        const val EVENT_KEY_LOADING = "loading"
//        const val EVENT_PAGE_STATUS = "pageStatus"
//        const val EVENT_REFRESH_STATUS = "refreshStatus"
    }
}

