package com.example.android.eyebody.management.config.subcontent.caller

import android.app.Activity
import com.example.android.eyebody.management.config.subcontent.CMBaseSubContent
import junit.framework.Assert

/**
 * Created by YOON on 2017-12-02
 * fragment 에서 fragment 를 call 하려면 DialogCallerSubContent 로 Down Casting 하여 사용
 */
abstract class CallableSubContent(text: String, preferenceName: String?, hasSwitch: Boolean,
                                  preferenceValueExplanation: List<String>? = null,
                                  private val callableList: List<Any?>?,
                                  val requestCode: Int=0) :
        CMBaseSubContent(
                text = text,
                preferenceName = preferenceName,
                hasSwitch = hasSwitch,
                preferenceValueExplanation = preferenceValueExplanation
        ) {
    init {
        Assert.assertTrue("Assertion failed : must both (preferenceValueExplanation, callableList) size are same\n" +
                "or both are contain equal or less than 1 storedInt\n",
                ((preferenceValueExplanation == null || preferenceValueExplanation.size <= 1) && (callableList == null || callableList.size <= 1))
                        || (callableList?.size == preferenceValueExplanation?.size))
    }

    open fun canCall(order: Int): Boolean = callableList?.get(order) != null
    abstract fun call(callerActivity: Activity, order: Int)
}
