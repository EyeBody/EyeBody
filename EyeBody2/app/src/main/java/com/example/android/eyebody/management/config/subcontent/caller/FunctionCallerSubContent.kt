package com.example.android.eyebody.management.config.subcontent.caller

import android.app.Activity
import android.util.Log

val TAG = "mydbg_ConfMNG_FunCalSC"

/**
 * @param function must use like Sync{ do something } or Async{ do something as Async }, Async will block click current content and execute not on UI thread.
 * @see Async Async must return setAsync(Int)
 * */

class FunctionCallerSubContent(text: String,
                               preferenceName: String?,
                               hasSwitch: Boolean,
                               preferenceValueExplanation: List<String>?,
                               val function: List<SyncOrAsyncFunction>? = null)
    : CallableSubContent(
        text = text,
        preferenceName = preferenceName,
        hasSwitch = hasSwitch,
        preferenceValueExplanation = preferenceValueExplanation,
        callableList = function,
        requestCode = 0
) {
    var ret: Int? = null
    private var returnListener: FunctionCallerReturnListener? = null


    /**
     * If you use Sync{...} you don't need it.
     * It is for Async task.
     */
    fun setOnReturnListener(returnListener: () -> Unit) {
        setOnReturnListener(object : FunctionCallerReturnListener {
            override fun onReturn() {
                returnListener()
            }
        })
    }


    /**
     * whenever must use it. if you change prefValue of contents.
     */
    fun setOnReturnListener(returnListener: FunctionCallerReturnListener) {
        this.returnListener = returnListener
    }


    override fun canCall(order: Int): Boolean = function?.get(order) != null


    private fun isAsync(order: Int): Boolean {
        val func = function?.get(order)
        return func is Async
    }

    override fun call(callerActivity: Activity, order: Int) {

        val func = function?.get(order)

        if (isAsync(order)) {
            (func as Async).funcCall(returnListener, this) // 클릭을 block 해야 댐.
        } else {
            (func as Sync).funcCall(returnListener, this)
        }

    }

}

sealed class SyncOrAsyncFunction

class Sync(val func: () -> Int) : SyncOrAsyncFunction() {
    val funcCall = { listener: FunctionCallerReturnListener?, caller: FunctionCallerSubContent ->
        caller.ret = func()
        listener?.onReturn()
    }
}

class Async(val func: () -> setAsyncReturn) : SyncOrAsyncFunction() {
    val funcCall = { listener: FunctionCallerReturnListener?, caller: FunctionCallerSubContent ->
        object : Thread() {
            override fun run() {
                caller.ret = run { func().ret }
                listener?.onReturn()
            }
        }.run()
    }
}

class setAsyncReturn(val ret: Int)
