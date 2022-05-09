package com.tyl.base_lib.widget.multiplestatus

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.lifecycle.*
import com.tyl.base_lib.R

class MultipleStatusLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IMultipleStatusLayout {

    override var status: IMultipleStatusView.Status = IMultipleStatusView.Status.Init
        set(value) {
            if (field == value) return
            val oldView = getViewByStatus(field)
            val newView = getViewByStatus(value)
            if (oldView != newView) {
                oldView?.visibility = View.GONE
                newView?.visibility = View.VISIBLE
            }
            field = value
        }

    private val stateViews = HashMap<IMultipleStatusView.Status, View>()
    private val stateViewLayoutIds = HashMap<IMultipleStatusView.Status, Int>()
    private val stateViewIds = HashMap<IMultipleStatusView.Status, Int>()

    private val mInflater: LayoutInflater

    init {
        @SuppressLint("CustomViewStyleable") val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.MultipleStatusLayout, defStyleAttr, 0)
        // layout
        stateViewLayoutIds[IMultipleStatusView.Status.Empty] =
            typedArray.getResourceId(R.styleable.MultipleStatusLayout_status_emptyView, defaultEmptyViewLayoutId)
        stateViewLayoutIds[IMultipleStatusView.Status.Error] =
            typedArray.getResourceId(R.styleable.MultipleStatusLayout_status_errorView, defaultErrorViewLayoutId)
        stateViewLayoutIds[IMultipleStatusView.Status.Loading] =
            typedArray.getResourceId(R.styleable.MultipleStatusLayout_status_loadingView, defaultLoadingViewLayoutId)
        stateViewLayoutIds[IMultipleStatusView.Status.Init] =
            typedArray.getResourceId(R.styleable.MultipleStatusLayout_status_initView, defaultInitViewLayoutId)
        // viewId
        stateViewIds[IMultipleStatusView.Status.Empty] = typedArray.getResourceId(R.styleable.MultipleStatusLayout_status_emptyViewId, NO_ID)
        stateViewIds[IMultipleStatusView.Status.Error] = typedArray.getResourceId(R.styleable.MultipleStatusLayout_status_errorViewId, NO_ID)
        stateViewIds[IMultipleStatusView.Status.Loading] = typedArray.getResourceId(R.styleable.MultipleStatusLayout_status_loadingViewId, NO_ID)
        stateViewIds[IMultipleStatusView.Status.Init] = typedArray.getResourceId(R.styleable.MultipleStatusLayout_status_initViewId, NO_ID)
        stateViewIds[IMultipleStatusView.Status.Content] = typedArray.getResourceId(R.styleable.MultipleStatusLayout_status_contentViewId, NO_ID)
        typedArray.recycle()
        mInflater = LayoutInflater.from(getContext())
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        stateViewIds.forEach { entry ->
            var view: View? = entry.value.takeIf { it != NO_ID }?.let { findViewById(it) }
            if (view == null && entry.key == IMultipleStatusView.Status.Content) {
                view = childCount.takeIf { it > 0 }?.let { getChildAt(it) }
            }
            view?.let {
                setViews(entry.key, view)
            }
        }

        status = IMultipleStatusView.Status.Init
    }

    override fun getViewByStatus(status: IMultipleStatusView.Status): View? {
        return stateViews[status] ?: generateStatusViewByLayoutId(status)
    }

    private fun generateStatusViewByLayoutId(status: IMultipleStatusView.Status): View? {
        return stateViewLayoutIds[status]?.takeIf { it > 0 }?.let {
            mInflater.inflate(it, this, false).apply {
                setViews(status, this)
            }
        }
    }

    override fun setViews(status: IMultipleStatusView.Status, view: View) {
        stateViews[status] = view
        view.visibility = View.GONE
        if (view.parent != this) {
            if (view.parent != null) {
                val parent = view.parent as? ViewGroup
                parent?.removeView(view)
            }
            addView(view)
        }
    }

    override fun <T> observerData(lifecycleOwner: LifecycleOwner, data: LiveData<T?>) {
        data.observe(lifecycleOwner, Observer {
            status = when (it) {
                null -> IMultipleStatusView.Status.Error
                is Collection<*> -> if (it.isEmpty()) IMultipleStatusView.Status.Empty else IMultipleStatusView.Status.Content
                is Map<*, *> -> if (it.isEmpty()) IMultipleStatusView.Status.Empty else IMultipleStatusView.Status.Content
                is Array<*> -> if (it.size == 0) IMultipleStatusView.Status.Empty else IMultipleStatusView.Status.Content
                is ByteArray -> if (it.size == 0) IMultipleStatusView.Status.Empty else IMultipleStatusView.Status.Content
                is CharArray -> if (it.size == 0) IMultipleStatusView.Status.Empty else IMultipleStatusView.Status.Content
                is ShortArray -> if (it.size == 0) IMultipleStatusView.Status.Empty else IMultipleStatusView.Status.Content
                is IntArray -> if (it.size == 0) IMultipleStatusView.Status.Empty else IMultipleStatusView.Status.Content
                is LongArray -> if (it.size == 0) IMultipleStatusView.Status.Empty else IMultipleStatusView.Status.Content
                is FloatArray -> if (it.size == 0) IMultipleStatusView.Status.Empty else IMultipleStatusView.Status.Content
                is DoubleArray -> if (it.size == 0) IMultipleStatusView.Status.Empty else IMultipleStatusView.Status.Content
                else -> IMultipleStatusView.Status.Content
            }
        })
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_CREATE -> {
                        status = IMultipleStatusView.Status.Init
                    }
                    Lifecycle.Event.ON_START -> {
                        status = IMultipleStatusView.Status.Loading
                    }
                    else -> {
                    }
                }
            }

        })
    }

    fun showInit() {
        status = IMultipleStatusView.Status.Init
    }

    fun showLoading() {
        status = IMultipleStatusView.Status.Loading
    }

    fun showEmpty(msg: CharSequence? = null) {
        status = IMultipleStatusView.Status.Empty
        (getViewByStatus(IMultipleStatusView.Status.Empty) as? IStatusHintView)?.setHint(msg)
    }

    fun showError(msg: CharSequence? = null, onClick: ((View) -> Unit)? = null) {
        status = IMultipleStatusView.Status.Error
        (getViewByStatus(IMultipleStatusView.Status.Error) as? IStatusHintView)?.setHint(msg)
        (getViewByStatus(IMultipleStatusView.Status.Error) as? IStatusClickableView)?.setOnClickListener(onClick)
    }

    fun showContent() {
        status = IMultipleStatusView.Status.Loading
    }

    fun setViews(status: IMultipleStatusView.Status, @LayoutRes resId: Int) {
        setViews(status, LayoutInflater.from(context).inflate(resId, null, false))
    }

    fun setContentView(@LayoutRes resId: Int) {
        setContentView(LayoutInflater.from(context).inflate(resId, null, false))
    }

    fun setContentView(view: View) {
        setViews(IMultipleStatusView.Status.Content, view)
    }

    companion object {
        private const val NO_LAYOUT = -1

        private const val NO_ID = -1

        private var defaultInitViewLayoutId: Int = NO_LAYOUT

        private var defaultLoadingViewLayoutId: Int = NO_LAYOUT

        private var defaultEmptyViewLayoutId: Int = NO_LAYOUT

        private var defaultErrorViewLayoutId: Int = NO_LAYOUT

        fun setDefaultInitViewLayoutId(@LayoutRes resId: Int) = apply {
            defaultInitViewLayoutId = resId
        }

        fun setDefaultLoadingViewLayoutId(@LayoutRes resId: Int) = apply {
            defaultLoadingViewLayoutId = resId
        }

        fun setDefaultEmptyViewLayoutId(@LayoutRes resId: Int) = apply {
            defaultEmptyViewLayoutId = resId
        }

        fun setDefaultErrorViewLayoutId(@LayoutRes resId: Int) = apply {
            defaultErrorViewLayoutId = resId
        }
    }
}