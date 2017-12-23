package com.example.android.eyebody.management.config.subcontent.caller

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity

/**
 * Created by YOON on 2017-12-02
 */
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
    /** attach call method to config storedInt on click listener
     */
    override fun canCall(order: Int): Boolean = activityJavaClass?.get(order) != null

    override fun call(callerActivity: Activity, order: Int) {
        val mIntent = Intent(callerActivity.baseContext, activityJavaClass?.get(order))
        callerActivity.startActivityForResult(mIntent, requestCode)
    }
}