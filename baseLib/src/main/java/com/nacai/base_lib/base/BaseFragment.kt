package com.nacai.base_lib.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.nacai.base_lib.tools.NoProguardClass

abstract class BaseFragment<B : ViewDataBinding> : Fragment(), IView, NoProguardClass {

    protected lateinit var binding: B
        private set

    //数据是否加载标识
    protected var isDataInitiated = false
        private set

    //view是否加载标识
    protected var isViewInitiated = false
        private set

    /**
     * 是否懒加载
     * @return true:是(默认) false:不
     */
    protected open fun lazyLoad() = true

    /**
     * 是否fragment显示的时候都重新加载数据
     */
    protected open fun autoRefreshData() = false

    private val backPressedDispatcher by lazy {
        requireActivity().onBackPressedDispatcher
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (!onPageBackPressed()) {
                isEnabled = false
                backPressedDispatcher.onBackPressed()
                isEnabled = true
            }
        }
    }

    /**
     * 返回按键回调事件
     * onPageBackPressed 的回调顺序会根据注册事件倒序调用，且采用责任链模式，
     * 即：从最后注册的 fragment 开始向前遍历，找到并调用 isEnabled=true 的 Callback,然后中断遍历
     * 这里进行了封装，此方法返回 false 时，会将此次 onPageBackPressed 的遍历重新开始，且暂时设置 isEnabled=false（循环结束之后会重新设置 isEnabled=true ）
     * 注意：此方法需要 activity 的配合才会生效，请使用 BaseActivity(或者让你的 Activity 使用 onBackPressedDispatcher)，请不要重写 Activity 的 onBackPressed()
     * @return true:拦截返回事件，false:交给上级处理
     */
    protected open fun onPageBackPressed(): Boolean {
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, null, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backPressedDispatcher.addCallback(this.viewLifecycleOwner, onBackPressedCallback)
        initView()
        //将view加载的标识设置为true
        isViewInitiated = true
        //判断是否懒加载
        if (!lazyLoad()) {
            initData()
            isDataInitiated = true
        }
    }

    override fun onResume() {
        super.onResume()
        // 以前的懒加载方案是重写 setUserVisibleHint() 方法，来监听真实的可见状态
        // 现在 google 官方已经将 setUserVisibleHint() 标记为了 Deprecated
        // 并提供了 FragmentTransaction#setMaxLifecycle(Fragment, Lifecycle.State) 方法，来控制真实的生命周期
        // ViewPager 和 ViewPager2 均已进行了兼容，正常需求下我们已经无需再做额外处理
        // 如果你需要在手动处理 fragment 的 show/hide add/remove , 请尝试使用 FragmentTransaction#setMaxLifecycle(Fragment, Lifecycle.State)
        prepareData()
    }

    /**
     * 懒加载的方法
     */
    private fun prepareData() {
        //通过判断各种标识去进行数据加载
        if (!isDataInitiated || autoRefreshData()) {
            initData()
            isDataInitiated = true
        }
    }

    override fun attachBaseVMEvent(viewModel: BaseViewModel) {
        viewModel.loadingEvent.observe(this, EventObserver {
            val showLoadingByIView: (view: IView, value: Any?) -> Unit = { view, value ->
                when (value) {
                    is String -> {
                        if (value.isNotEmpty()) {
                            view.showLoading(true, value)
                        } else {
                            view.showLoading(false)
                        }
                    }
                    true -> {
                        view.showLoading(true)
                    }
                    false -> {
                        view.showLoading(false)
                    }
                }
            }
            val iView: IView = (activity as? BaseActivity<*>) ?: this
            showLoadingByIView(iView, it)
        })
    }
}