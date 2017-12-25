package com.example.android.eyebody.management.config.subcontent.caller

import android.app.Activity
import com.example.android.eyebody.management.config.subcontent.callee.StorableDialogFragment

/**
 * @param text: String
 * @param preferenceName: String?
 * @param hasSwitch: Boolean
 * @param preferenceValueExplanation: List<String>?
 * @param storableDialog: List<StorableDialogFragment?>?
 * @param requestCode: Int
 */
class DialogCallerSubContent(text: String, preferenceName: String?, hasSwitch: Boolean,
                             preferenceValueExplanation: List<String>? = null,
                             val storableDialog: List<StorableDialogFragment?>?) :
        CallableSubContent(
                text = text,
                preferenceName = preferenceName,
                hasSwitch = hasSwitch,
                preferenceValueExplanation = preferenceValueExplanation,
                callableList = storableDialog
        ) {
    override fun canCall(order: Int): Boolean = storableDialog?.get(order) != null

    override fun call(callerActivity: Activity, order: Int) {
        val fragManager = callerActivity.fragmentManager
        storableDialog?.get(order)?.show(fragManager, "DialogCallerSubContent")
    }
}


