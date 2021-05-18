package com.tyl.base_lib.tools

import androidx.appcompat.app.AppCompatActivity
import java.util.*

object ActivityStack {
    val activityList: LinkedList<AppCompatActivity> = LinkedList<AppCompatActivity>()
        get() {
            return LinkedList(field)
        }

    fun addActivity(activity: AppCompatActivity) {
        activityList.add(activity)
    }

    fun removeActivity(a: AppCompatActivity) {
        activityList.remove(a)
    }

    fun getTopActivity(): AppCompatActivity? {
        return activityList.peekLast()
    }
}