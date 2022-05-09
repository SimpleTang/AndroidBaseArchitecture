package com.tyl.base_lib.base

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StyleRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.tyl.base_lib.R
import com.tyl.base_lib.tools.NoProguardClass

abstract class BaseDialogFragment<B : ViewDataBinding> : DialogFragment(), IView, NoProguardClass {

    companion object {
        const val RESIZE_WIDTH = 0
        const val RESIZE_HEIGHT = 1
        const val RESIZE_GRAVITY = 2
    }

    protected lateinit var binding: B
        private set

    @StyleRes
    protected open fun getStyle(): Int = R.style.Translucent_NoTitle_Default

    /**
     * 设置 dialog 大小
     * @param size 一个长度为 3 个数组 按下标顺序 0:宽度，1:高度，2:对齐方式
     * 默认高宽为 ViewGroup.LayoutParams.WRAP_CONTENT ，对其方式为 Gravity.CENTER
     */
    protected open fun resize(size: IntArray) {}

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, getStyle())
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val size = intArrayOf(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
            resize(size)
            it.window?.setLayout(size[0], size[1])
            it.window?.setGravity(size[2])
        }
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
        initData()
    }

    override fun attachBaseVMEvent(viewModel: BaseViewModel) {
        viewModel.loadingEvent.observe(this, EventObserver {
            val showLoadingByIView: (view: IView, value: Any?) -> Unit = { view, value ->
                when (value) {
                    is String -> {
                        if (value.isNotEmpty()) {
                            view.setLoadingDialog(true, value)
                        } else {
                            view.setLoadingDialog(false)
                        }
                    }
                    true -> {
                        view.setLoadingDialog(true)
                    }
                    false -> {
                        view.setLoadingDialog(false)
                    }
                }
            }
            val iView: IView = (activity as? BaseActivity<*>) ?: this
            showLoadingByIView(iView, it)
        })
    }

    override fun setLoadingDialog(show: Boolean, msg: String) {
        (activity as? BaseActivity<*>)?.setLoadingDialog(show, msg)
    }
}