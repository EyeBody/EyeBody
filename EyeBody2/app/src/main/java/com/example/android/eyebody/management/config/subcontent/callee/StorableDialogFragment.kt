package com.example.android.eyebody.management.config.subcontent.callee

import android.app.DialogFragment
import android.content.DialogInterface

/**
 * Created by YOON on 2017-12-02
 */
open class StorableDialogFragment : DialogFragment() {
    var storedInt: Int? = null
    private var dismissListener : StorableDialogFragmentDismissListener? = null

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        dismissListener?.onDismiss()
    }

    fun setOnDismissListener(dismissListener: () -> Unit){
        val listener = object : StorableDialogFragmentDismissListener {
            override fun onDismiss(){
                dismissListener.invoke()
            }
        }
        setOnDismissListener(listener)
    }

    fun setOnDismissListener(dismissListener: StorableDialogFragmentDismissListener){
        this.dismissListener = dismissListener
    }
}