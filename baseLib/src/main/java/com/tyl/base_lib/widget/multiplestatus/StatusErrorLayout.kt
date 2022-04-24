package com.tyl.base_lib.widget.multiplestatus

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.tyl.base_lib.R

class StatusErrorLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), IStatusHintView, IStatusDrawableView,
    IStatusClickableView {
    private var isFinishFind = false

    private var textView: TextView? = null
    private var imageView: ImageView? = null
    private var clickableView: View? = null

    private var hintViewId: Int = -1
    private var drawableViewId: Int = -1
    private var clickableViewId: Int = -1

    init {
        @SuppressLint("CustomViewStyleable") val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.ViewGroup, defStyleAttr, 0)
        hintViewId =
            typedArray.getResourceId(R.styleable.ViewGroup_status_hintViewId, -1)
        drawableViewId =
            typedArray.getResourceId(R.styleable.ViewGroup_status_drawableViewId, -1)
        clickableViewId =
            typedArray.getResourceId(R.styleable.ViewGroup_status_clickableViewId, -1)
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        textView = findViewById(hintViewId)
        imageView = findViewById(drawableViewId)
        clickableView = findViewById(clickableViewId)
    }

    private fun findView(parent: ViewGroup) {
        if (isFinishFind) {
            return
        }
        for (v in parent.children) {
            if (textView != null && imageView != null)
                return

            if (textView == null && v is TextView) {
                textView = v
                continue
            }
            if (imageView == null && v is ImageView) {
                imageView = v
                continue
            }
            if (clickableView == null && v is Button) {
                clickableView = v
                continue
            }
            if (v is ViewGroup) {
                findView(v)
            }
        }
        if (clickableView == null)
            clickableView = children.firstOrNull()
        if (parent === this) {
            isFinishFind = true
        }
    }

    override fun setDrawable(drawable: Drawable?) {
        if (imageView == null)
            findView(this)
        imageView?.setImageDrawable(drawable)
    }

    override fun setHint(msg: CharSequence?) {
        if (textView == null)
            findView(this)
        textView?.text = msg
    }

    override fun setOnClickListener(onClick: ((View) -> Unit)?) {
        if (clickableView == null)
            findView(this)
        onClick?.let {
            clickableView?.setOnClickListener(it)
        }
    }

}