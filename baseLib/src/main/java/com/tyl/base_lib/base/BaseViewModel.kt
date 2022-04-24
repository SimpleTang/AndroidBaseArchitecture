package com.tyl.base_lib.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
<<<<<<< HEAD
import com.tyl.base_lib.widget.multistate.PageStatus
import com.tyl.base_lib.widget.refresh.RefreshStatus
=======
>>>>>>> v0.0.1

open class BaseViewModel : ViewModel() {
    val events = HashMap<String, MutableLiveData<VMEvent<*>>>()
    val closePageEvent: MutableLiveData<VMEvent<*>> = MutableLiveData()
    val loadingEvent: MutableLiveData<VMEvent<*>> = MutableLiveData()
<<<<<<< HEAD
    val defaultPageStatus: MutableLiveData<PageStatus> = MutableLiveData()
    val defaultRefreshStatus: MutableLiveData<RefreshStatus> = MutableLiveData()
=======
>>>>>>> v0.0.1

    init {
    }

    companion object {
//        const val EVENT_KEY_CLOSE_PAGE = "closePage"
//        const val EVENT_KEY_LOADING = "loading"
//        const val EVENT_PAGE_STATUS = "pageStatus"
//        const val EVENT_REFRESH_STATUS = "refreshStatus"
    }
}

