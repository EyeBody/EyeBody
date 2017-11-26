package com.example.android.eyebody.management.config

import android.app.Activity
import android.app.DialogFragment
import android.app.Fragment
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import junit.framework.Assert

/**
 * Created by YOON on 2017-11-19
 */

/**
 * preference are saved false or true
 * actually it saved 0, 1, 2, 3, ~.
 *
 * switch used when need boolean type selector,
 * switch not used when need string type selector (using dialog)
 * @param preferenceValueExplanation optional param, {false, true} for explanation string when using switch, {preferenceValueList} for explanation when not using switch.
 */
open class ConfigManagementSubContent(val text: String = "", val hasSwitch: Boolean, val preferenceName: String?,
                                      val preferenceValueExplanation: List<String>? = null) {
    /*
    text 가 큰 글씨로 써지고
    그 밑에 preferenceValueExplanation 이 써짐 (현재 value 에 따라 출력, null 일 경우 안 씀)
     */
    init {
        Assert.assertTrue("Assertion failed : more than 1 preferenceValueExplanation needs preferenceName\n" +
                "if not needed, preferenceName must be null\n" +
                "preferenceName : is null : ${preferenceName == null}\n" +
                "preferenceValueExplanation : size : ${preferenceValueExplanation?.size ?: "null"}",
                (preferenceName == null) == (preferenceValueExplanation == null || preferenceValueExplanation.size <= 1))
    }
}

class SwitchableSubContent(text: String, preferenceName: String,
                           preferenceValueExplanation: List<String>? = null) :
        ConfigManagementSubContent(
                text = text,
                preferenceName = preferenceName,
                hasSwitch = true,
                preferenceValueExplanation = preferenceValueExplanation
        )

/**
 * fragment 에서 fragment 를 call 하려면 DialogCallerSubContent 로 Down Casting 하여 사용
 */
abstract class CallableSubContent(text: String, preferenceName: String?, hasSwitch: Boolean,
                                  preferenceValueExplanation: List<String>? = null,
                                  private val callableList: List<Any?>?,
                                  val requestCode: Int) :
        ConfigManagementSubContent(
                text = text,
                preferenceName = preferenceName,
                hasSwitch = hasSwitch,
                preferenceValueExplanation = preferenceValueExplanation
        ) {
    init {
        Assert.assertTrue("Assertion failed : must both (preferenceValueExplanation, callableList) size are same\n" +
                "or both are contain equal or less than 1 item\n",
                ((preferenceValueExplanation == null || preferenceValueExplanation.size <= 1) && (callableList == null || callableList.size <= 1))
                        || (callableList?.size == preferenceValueExplanation?.size))
    }

    abstract fun canCall(order: Int): Boolean
    abstract fun call(callerActivity: Activity, order: Int)
}

class DialogCallerSubContent(text: String, preferenceName: String?, hasSwitch: Boolean,
                             preferenceValueExplanation: List<String>? = null,
                             private val dialogFragment: List<DialogFragment?>?,
                             requestCode: Int = 0) : //TODO : request Code 있으면 좋을거같은데 호출을 하는 방법??
        CallableSubContent(
                text = text,
                preferenceName = preferenceName,
                hasSwitch = hasSwitch,
                preferenceValueExplanation = preferenceValueExplanation,
                callableList = dialogFragment,
                requestCode = requestCode
        ) {
    override fun canCall(order: Int): Boolean = dialogFragment?.get(order) != null

    override fun call(callerActivity: Activity, order: Int) {
        val fragManager = callerActivity.fragmentManager
        dialogFragment?.get(order)?.show(fragManager, "DialogCallerSubContent")
    }

    fun call(callerFragment: Fragment, order: Int) {
        val fragManager = callerFragment.fragmentManager
        dialogFragment?.get(order)?.show(fragManager, "DialogCallerSubContent")
    }
}

class ActivityCallerSubContent(text: String, preferenceName: String?, hasSwitch: Boolean,
                               preferenceValueExplanation: List<String>? = null,
                               private val activityJavaClass: List<Class<AppCompatActivity>?>?,
                               requestCode: Int = 0) :
        CallableSubContent(
                text = text,
                preferenceName = preferenceName,
                hasSwitch = hasSwitch,
                preferenceValueExplanation = preferenceValueExplanation,
                callableList = activityJavaClass,
                requestCode = requestCode
        ) {
    /** attach call method to config item on click listener
     */
    override fun canCall(order: Int): Boolean = activityJavaClass?.get(order) != null

    override fun call(callerActivity: Activity, order: Int) {
        val mIntent = Intent(callerActivity.baseContext, activityJavaClass?.get(order))
        callerActivity.startActivityForResult(mIntent, requestCode)
    }
}
