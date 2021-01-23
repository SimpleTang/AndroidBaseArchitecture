package com.nacai.base_lib.base

import androidx.lifecycle.Observer

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 *
 */
open class VMEvent<out T>(private val content: T) {

    @Suppress("MemberVisibilityCanBePrivate")
    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}

/**
 * An [Observer] for [VMEvent]s, simplifying the pattern of checking if the [VMEvent]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [VMEvent]'s contents has not been handled.
 */
class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) : Observer<VMEvent<T>> {
    override fun onChanged(event: VMEvent<T>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}

fun <T> event(value: T): VMEvent<T> {
    return VMEvent(value)
}

fun emptyEvent() = event(Unit)