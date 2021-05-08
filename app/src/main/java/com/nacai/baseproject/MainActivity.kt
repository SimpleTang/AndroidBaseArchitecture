package com.nacai.baseproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.nacai.base_lib.base.EventObserver
import com.nacai.base_lib.base.VMEvent
import com.nacai.base_lib.base.event
import com.nacai.base_lib.extens.onClick
import com.nacai.base_lib.provider.ProviderManager
import com.nacai.base_lib.widget.multistate.PageStatus
import com.nacai.baseproject.databinding.ActivityMainBinding
import com.nacai.common.provider.MainProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    val state = MutableLiveData<VMEvent<PageStatus>>(event(PageStatus.NORMAL))
    lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.lifecycleOwner = this
        mBinding.aty = this
        findViewById<Button>(R.id.btn).onClick {
//            ProviderManager.get<MainProvider>().show("MainActivity Call")
//            if (state.value?.peekContent() == PageStatus.ERROR) {
//                state.value = event(PageStatus.CONTENT)
//            } else {
//                state.value = event(PageStatus.ERROR)
//            }
            repeat(10){
                lifecycleScope.launch {
                    withContext(Dispatchers.IO){
                        ProviderManager.get<MainProvider>().show("MainActivity Call:$it")
                    }
                }
            }
        }
    }
}