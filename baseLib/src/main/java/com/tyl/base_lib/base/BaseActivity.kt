package com.tyl.base_lib.base

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.tyl.base_lib.tools.ActivityStack
import com.tyl.base_lib.tools.NoProguardClass

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity(), NoProguardClass, IView {

    protected lateinit var binding: B

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (!onPageBackPressed()) {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
                isEnabled = true
            }
        }
    }

    /**
     * 重写此方法，而不是 onBackPressed()
     */
    protected open fun onPageBackPressed(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBaseInit()
    }

    /**
     * 使用新的方法而不是在 onCreate 中直接写，是为了子类后期好扩展，
     * 比如需要在onCreate之后，但在此之前做一些事情
     */
    private fun onBaseInit() {
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        /* 这里不要使用 #addCallback(LifecycleOwner, OnBackPressedCallback) 否则会在 activity 切换之后造成 onBackPressedCallback 顺序不对，
          从而会造成 activity 的回调永远会在 fragment 之前 */
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        initView()
        initData()
        ActivityStack.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityStack.removeActivity(this)
    }

    override fun getResources(): Resources? {
        // 屏蔽系统设置字体大小的影响
        var resources = super.getResources()
        val newConfig = resources?.configuration
        val displayMetrics: DisplayMetrics = resources.displayMetrics
        if (resources != null && newConfig != null && newConfig.fontScale != 1f) {
            newConfig.fontScale = 1f
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val configurationContext = createConfigurationContext(newConfig)
                resources = configurationContext.resources
                displayMetrics.scaledDensity = displayMetrics.density * newConfig.fontScale
            } else {
                resources.updateConfiguration(newConfig, displayMetrics)
            }
        }
        return resources
    }

    override fun attachBaseVMEvent(viewModel: BaseViewModel) {
        viewModel.loadingEvent.observe(this, EventObserver {
            when (it) {
                is String -> {
                    if (it.isNotEmpty()) {
                        setLoadingDialog(true, it)
                    } else {
                        setLoadingDialog(false)
                    }
                }
                true -> {
                    setLoadingDialog(true)
                }
                false -> {
                    setLoadingDialog(false)
                }
            }
        })
        viewModel.closePageEvent.observe(this,EventObserver{
            it?.let { onBackPressed() }
        })
    }
}