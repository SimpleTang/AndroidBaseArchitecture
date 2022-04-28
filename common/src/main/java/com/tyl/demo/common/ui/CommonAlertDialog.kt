package com.tyl.demo.common.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tyl.demo.common.ext.showDialog
import com.tyl.base_lib.base.BaseDialogFragment
import com.tyl.base_lib.base.application
import com.tyl.base_lib.extens.dp
import com.tyl.base_lib.extens.onClick
import com.tyl.base_lib.tools.L
import com.tyl.demo.common.R
import com.tyl.demo.common.databinding.CommonDialogAlertTextBinding

class CommonAlertDialog private constructor() : BaseDialogFragment<CommonDialogAlertTextBinding>() {
    override val layoutId: Int
        get() = R.layout.common_dialog_alert_text

    override fun resize(size: IntArray) {
        super.resize(size)
        size[RESIZE_WIDTH] = 300.dp
    }

    private var canClose = true
    private var msg: CharSequence? = null
    private var cancelBtnText: CharSequence? = null
    private var confirmBtnText: CharSequence? = null
    private var cancelBtnCall: ((CommonAlertDialog) -> Unit)? = null
    private var confirmBtnCall: ((CommonAlertDialog) -> Unit)? = null

    override fun initView() {
    }

    override fun initData() {
        canClose = arguments?.getBoolean("canClose") == true
        cancelBtnText = arguments?.getCharSequence("cancelBtnText")
        confirmBtnText = arguments?.getCharSequence("confirmBtnText")
        msg = arguments?.getCharSequence("msg")

        if (msg.isNullOrEmpty()) {
            dismissAllowingStateLoss()
            L.e("CommonAlertDialog dismissed , msg is null or Empty!")
            return
        }

        binding.tvMsg.text = msg
        if (cancelBtnText.isNullOrEmpty()) {
            binding.tvCancel.visibility = View.GONE
        } else {
            binding.tvCancel.visibility = View.VISIBLE
            binding.tvCancel.text = cancelBtnText
            binding.tvCancel.onClick {
                dismissAllowingStateLoss()
                cancelBtnCall?.invoke(this)
            }
        }

        binding.tvConfirm.onClick {
            dismissAllowingStateLoss()
            confirmBtnCall?.invoke(this)
        }

        binding.tvConfirm.text = confirmBtnText

        isCancelable = canClose

    }

    companion object {
        fun show(fm: FragmentManager, call: Builder.() -> Unit) {
            val builder = Builder(fm)
            call(builder)
            builder.show()
        }
    }

    class Builder(private val fm: FragmentManager) {
        private var canClose = true
        private var msg: CharSequence? = null
        private var cancelBtnText: CharSequence? = null
        private var confirmBtnText: CharSequence? = null
        private var cancelBtnCall: ((CommonAlertDialog) -> Unit)? = null
        private var confirmBtnCall: ((CommonAlertDialog) -> Unit)? = null
        private var dialog: CommonAlertDialog? = null

        fun setCanClose(canClose: Boolean) = apply {
            this.canClose = canClose
        }

        fun setMsg(msg: CharSequence) = apply {
            this.msg = msg
        }

        fun setCancelBtn(
            btnText: CharSequence? = application.getText(R.string.common_cancel),
            btnCall: ((CommonAlertDialog) -> Unit)? = null
        ) = apply {
            this.cancelBtnText = btnText
            this.cancelBtnCall = btnCall
        }

        fun setConfirm(
            btnText: CharSequence = application.getText(R.string.common_confirm),
            btnCall: ((CommonAlertDialog) -> Unit)? = null
        ) = apply {
            this.confirmBtnText = btnText
            this.confirmBtnCall = btnCall
        }

        fun show() {
            if (dialog == null) {
                dialog = CommonAlertDialog().also {
                    val arguments = Bundle()
                    arguments.putBoolean("canClose", canClose)
                    arguments.putCharSequence("msg", msg)
                    arguments.putCharSequence("cancelBtnText", cancelBtnText)
                    arguments.putCharSequence("confirmBtnText", confirmBtnText)
                    it.arguments = arguments
                    it.cancelBtnCall = cancelBtnCall
                    it.confirmBtnCall = confirmBtnCall
                    fm.showDialog(it)
                }
            }
        }
    }
}