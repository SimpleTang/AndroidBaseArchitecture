package com.nacai.base_lib.widget.multistate

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.core.view.children
import com.nacai.base_lib.R

class MultiStateLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        @LayoutRes
        private var defaultEmptyViewLayoutId: Int = 0

        @LayoutRes
        private var defaultLoadErrorViewLayoutId: Int = 0

        @LayoutRes
        private var defaultNetworkErrorViewLayoutId: Int = 0

        @LayoutRes
        private var defaultLoadingViewLayoutId: Int = 0

    }

    interface OnViewInitCallBack {
        fun onAttach(view: View, state: PageStatus)
    }

    class Builder {
        fun setDefaultEmptyViewLayoutId(@LayoutRes id: Int): Builder {
            defaultEmptyViewLayoutId = id
            return this
        }

        fun setDefaultLoadErrorViewLayoutId(@LayoutRes id: Int): Builder {
            defaultLoadErrorViewLayoutId = id
            return this
        }

        fun setDefaultNetworkErrorViewLayoutId(@LayoutRes id: Int): Builder {
            defaultNetworkErrorViewLayoutId = id
            return this
        }

        fun setDefaultLoadingViewLayoutId(@LayoutRes id: Int): Builder {
            defaultLoadingViewLayoutId = id
            return this
        }

        fun build() {}
    }


    var viewInitCallBack: OnViewInitCallBack? = null

    @LayoutRes
    private val mEmptyViewLayoutId: Int

    @LayoutRes
    private val mLoadErrorViewLayoutId: Int

    @LayoutRes
    private val mNetworkErrorViewLayoutId: Int

    @LayoutRes
    private val mLoadingViewLayoutId: Int

    @IdRes
    private val mContentViewId: Int

    private val mInflater: LayoutInflater

    private val stateViews = HashMap<PageStatus, View>()

    var status: PageStatus = PageStatus.NORMAL
        set(value) {
            if (tryShowStatus(value)) {
                field = value
            }
        }

    init {
        @SuppressLint("CustomViewStyleable") val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.MultiStateLayout, defStyleAttr, 0)
        mEmptyViewLayoutId =
            typedArray.getResourceId(
                R.styleable.MultiStateLayout_state_emptyView,
                defaultEmptyViewLayoutId
            )
        mLoadErrorViewLayoutId = typedArray.getResourceId(
            R.styleable.MultiStateLayout_state_loadErrorView,
            defaultLoadErrorViewLayoutId
        )
        mLoadingViewLayoutId = typedArray.getResourceId(
            R.styleable.MultiStateLayout_state_loadingView,
            defaultNetworkErrorViewLayoutId
        )
        mNetworkErrorViewLayoutId = typedArray.getResourceId(
            R.styleable.MultiStateLayout_state_networkErrorView,
            defaultLoadingViewLayoutId
        )
        mContentViewId =
            typedArray.getResourceId(R.styleable.MultiStateLayout_state_contentViewId, -1)
        typedArray.recycle()
        mInflater = LayoutInflater.from(getContext())
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val contentView = when {
            mContentViewId != -1 -> {
                findViewById(mContentViewId)
            }
            childCount > 0 -> getChildAt(0)
            else -> null
        }
        contentView?.let {
            stateViews[PageStatus.CONTENT] = it
            it.visibility = View.GONE
        }
    }

    private fun checkView(state: PageStatus) {
        when (state) {
            PageStatus.EMPTY -> {
                generateStateView(mEmptyViewLayoutId, state)
            }
            PageStatus.LOADING -> {
                generateStateView(mLoadingViewLayoutId, state)
            }
            PageStatus.ERROR -> {
                generateStateView(mLoadErrorViewLayoutId, state)
            }
            PageStatus.NO_NETWORK -> {
                generateStateView(mNetworkErrorViewLayoutId, state)
            }
            else -> {
            }
        }
    }

    private fun generateStateView(layoutId: Int, state: PageStatus): View? {
        var stateView = stateViews[state]
        if (stateView == null && layoutId != 0) {
            stateView = mInflater.inflate(layoutId, null)
            var viewIndex = childCount
            for (i in viewIndex downTo 1) {
                val childState = findStateByView(getChildAt(i - 1)) ?: continue
                if (state.level >= childState.level) {
                    viewIndex = i
                    break
                }
            }
            stateViews[state] = stateView
            addView(stateView, viewIndex)
            stateView?.visibility = View.GONE
            viewInitCallBack?.onAttach(stateView, state)
        }
        return stateView
    }

    private fun tryShowStatus(state: PageStatus): Boolean {
        if (this.status.level > state.level) return false
        checkView(state)
        var isShow = false
        for (view in children) {
            val viewState = findStateByView(view) ?: continue
            if (viewState == state) {
                view.visibility = View.VISIBLE
                isShow = true
            } else if (viewState.level <= state.level) {
                view.visibility = View.GONE
            }
        }
        return isShow
    }

    private fun findStateByView(view: View): PageStatus? {
        stateViews.forEach {
            if (it.value == view) return it.key
        }
        return null
    }

    fun getViewByState(state: PageStatus): View? {
        return stateViews[state]
    }
}