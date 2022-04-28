package com.tyl.ledger.common.ui.widget

import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.tyl.base_lib.base.application
import com.tyl.demo.common.R

data class TitleBarInfo(val title: CharSequence) {
    var showRightButton = MutableLiveData<Boolean>()
    var rightButtonText = MutableLiveData<String>()
    var rightButtonColor = MutableLiveData<Int>()
    var titleColor = MutableLiveData<Int>()

    init {
        titleColor.value = (ContextCompat.getColor(application, R.color.common_defaultTextColor))
        rightButtonColor.value =
            (ContextCompat.getColor(application, R.color.common_subTextColor))
    }

    constructor(title: String, rightButtonText: String) : this(title) {
        this.showRightButton.value = (true)
        this.rightButtonText.value = (rightButtonText)
    }

    constructor(title: String, rightButtonText: String, rightButtonColor: Int) : this(
        title,
        rightButtonText
    ) {
        this.rightButtonColor.value = (rightButtonColor)
    }

    constructor(title: String, titleColor: Int) : this(title) {
        this.titleColor.value = (titleColor)
    }

    constructor(
        title: String,
        titleColor: Int,
        rightButtonText: String,
        rightButtonColor: Int
    ) : this(title) {
        this.titleColor.value = (titleColor)
        this.showRightButton.value = (true)
        this.rightButtonText.value = (rightButtonText)
        this.rightButtonColor.value = (rightButtonColor)
    }
}