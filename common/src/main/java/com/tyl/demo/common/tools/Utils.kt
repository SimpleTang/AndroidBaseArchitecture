package com.tyl.demo.common.tools

import android.content.Intent
import android.os.Build
import java.util.*

object Utils {
    /**
     * 生成设备唯一id
     */
    @Suppress("DEPRECATION")
    fun getUniquePsuedoID(): String {
        val devIDShort = ("35"
                + Build.BOARD.length % 10 //主板
                + Build.BRAND.length % 10 //系统定制商
                + Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 //设备参数
                + Build.DISPLAY.length % 10 //显示屏参数
                + Build.HOST.length % 10 //Host值
                + Build.ID.length % 10 //修订版本列表
                + Build.MANUFACTURER.length % 10 //硬件制造商
                + Build.MODEL.length % 10 //版本
                + Build.PRODUCT.length % 10 //手机产品名
                + Build.TAGS.length % 10 //描述Build的标签
                + Build.TYPE.length % 10 //Builder类型
                + Build.USER.length % 10) //13 位  //User名
        val serial = try {
            Build::class.java.getField("SERIAL").get(null)?.toString() //API>=9 使用serial号  //硬件序列号
        } catch (exception: Exception) {
            //serial需要一个初始化
            "serial" // 随便一个初始化
        }
        val str = UUID(devIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
        return MD5.getMD5(str)
    }
}

object InviteUtils {

    fun checkInviteData(intent: Intent?) {
        intent?.data?.let {
            if (it.scheme == "djms" && it.host == "com.nacai.youli") {
                val data = it.getQueryParameter("d")
                intentInviteData = data
            }
        }
    }

    var intentInviteData: String? = null
        get() {
            val d = field
            //获取一次后清空
            intentInviteData = null
            return d
        }
}