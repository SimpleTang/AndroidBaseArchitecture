package com.tyl.base_lib.widget.multiplestatus

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.tyl.base_lib.R
import com.tyl.base_lib.widget.multiplestatus.IStatusDrawableView
import com.tyl.base_lib.widget.multiplestatus.IStatusHintView

class StatusEmptyLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), IStatusHintView, IStatusDrawableView {
    private var isFinishFind = false

    private var textView: TextView? = null
    private var imageView: ImageView? = null
    private var hintViewId:Int = -1
    private var drawableViewId:Int = -1

    init {
        @SuppressLint("CustomViewStyleable") val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.ViewGroup, defStyleAttr, 0)
        hintViewId =
            typedArray.getResourceId(R.styleable.ViewGroup_status_hintViewId, -1)
        drawableViewId =
            typedArray.getResourceId(R.styleable.ViewGroup_status_drawableViewId, -1)
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        textView = findViewById(hintViewId)
        imageView = findViewById(drawableViewId)
    }

    private fun findView(parent:ViewGroup){
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

            if(v is ViewGroup){
                findView(v)
            }
        }
        if(parent === this){
            isFinishFind = true
        }
    }

    override fun setDrawable(drawable: Drawable?) {
        if(imageView == null)
            findView(this)
        imageView?.setImageDrawable(drawable)
    }

    override fun setHint(msg: CharSequence?) {
        if(textView == null)
            findView(this)
        textView?.text = msg
    }

}