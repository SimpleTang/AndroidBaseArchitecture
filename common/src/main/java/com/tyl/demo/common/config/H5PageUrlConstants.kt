package com.tyl.demo.common.config

import com.tyl.demo.common.BuildConfig


object H5PageUrlConstants {
    //静态h5
    private const val RESOURCE_BASE_URL = "https://res.maimaichat.com/maimai2"
    private const val RESOURCE_BASE_URL_HELP = "$RESOURCE_BASE_URL/help"
    const val ABOUT_MAIMAI = "$RESOURCE_BASE_URL_HELP/aboutMaiMai_android.html"//关于麦麦
    const val WITHDRAW_RULES = "$RESOURCE_BASE_URL_HELP/transferOut.html"//提现说明规则
    const val COMMON_PROBLEM = "$RESOURCE_BASE_URL_HELP/commonProblom.html"//常见问题页面
    const val MONEY_AGREEMENT = "$RESOURCE_BASE_URL_HELP/currencyPurchaseAgreement.html"//货币购买协议
    const val PRIVACY_POLICY = "$RESOURCE_BASE_URL_HELP/privacyPolicy.html" //隐私协议
    const val USER_POLICY = "$RESOURCE_BASE_URL_HELP/userAgreement.html" //用户协议

    private const val RESOURCE_BASE_URL_RECORD = "${BuildConfig.H5_HOST}maimai2/record"
    const val RECHARGE_RECORD = "$RESOURCE_BASE_URL_RECORD/charge" // 充值记录
    const val CONSUME_RECORD = "$RESOURCE_BASE_URL_RECORD/consume" // 消费记录

    //业务的h5
    const val WITHDRAW = "${BuildConfig.H5_HOST}withdraw/" //提现界面
}