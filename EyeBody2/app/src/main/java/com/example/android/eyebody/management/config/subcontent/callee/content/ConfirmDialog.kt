package com.example.android.eyebody.management.config.subcontent.callee.content

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.android.eyebody.R
import com.example.android.eyebody.management.config.subcontent.callee.StorableDialogFragment

/**
 * Created by YOON on 2017-12-02
 */
class ConfirmDialog : StorableDialogFragment() {

    var title = "NULL"
    var msg = "NULL"
    lateinit var confirmMessage: String
    lateinit var cancelMessage: String

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (arguments != null) {
            title = arguments.getString(TITLE, title)
            msg = arguments.getString(MSG, msg)
            confirmMessage = arguments.getString(CONFIRM_MSG, "confirm")
            cancelMessage = arguments.getString(CANCEL_MSG, "cancel")
        }
        val mView = inflater?.inflate(R.layout.dialog_confirm, container)
        if(mView != null) {
            val vTitle = mView.findViewById<TextView>(R.id.textView_TitleMsg)
            val vConfirmMsg = mView.findViewById<TextView>(R.id.textView_ConfirmMsg)
            val vConfirmButton = mView.findViewById<Button>(R.id.button_Confirm)
            val vCancelButton = mView.findViewById<Button>(R.id.button_Cancel)

            vTitle.text = title
            vConfirmMsg.text = msg
            vConfirmButton.text = confirmMessage
            vCancelButton.text = cancelMessage

            vConfirmButton.setOnClickListener {
                storedInt = 1
                dismiss()
            }
            vCancelButton.setOnClickListener {
                storedInt = 0
                dismiss()
            }
        }
        return mView!!
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        /*
        storedInt = 0 //취소
        storedInt = 1 //허가
        storedInt = k //특정 용도
        */
    }

    companion object {
        private val TITLE = "param1"
        private val MSG = "param2"
        private val CONFIRM_MSG = "param3"
        private val CANCEL_MSG = "param4"
    }

    class Builder(private val title: String, private val msg: String) {

        private var confirmMessage = "확인"
        private var cancelMessage = "취소"

        fun setConfirmCancelMessage(confirmMsg: String, cancelMsg: String): Builder {
            this.confirmMessage = confirmMsg
            this.cancelMessage = cancelMsg
            return this
        }

        fun build(): StorableDialogFragment {
            val dialog = ConfirmDialog()
            val bundle = Bundle()
            bundle.putString(TITLE, title)
            bundle.putString(MSG, msg)
            bundle.putString(CONFIRM_MSG, confirmMessage)
            bundle.putString(CANCEL_MSG, cancelMessage)
            dialog.arguments = bundle
            return dialog
        }
    }
}