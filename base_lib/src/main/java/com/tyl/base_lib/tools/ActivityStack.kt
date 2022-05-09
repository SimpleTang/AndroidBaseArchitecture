package com.tyl.base_lib.tools

import androidx.appcompat.app.AppCompatActivity
import java.util.*

/**
 * 项目内的 Activity 栈，第三方SDK的 Activity 不会被记录
 */
object ActivityStack {

    private val atys: LinkedList<AppCompatActivity> = LinkedList<AppCompatActivity>()

    val activityList: LinkedList<AppCompatActivity>
        get() {
            return LinkedList(atys)
        }

    fun addActivity(activity: AppCompatActivity) {
        atys.add(activity)
    }

    fun removeActivity(a: AppCompatActivity) {
        atys.remove(a)
    }

    fun getTopActivity(): AppCompatActivity? {
        return atys.peekLast()
    }
}