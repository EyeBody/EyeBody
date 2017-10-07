package com.example.android.eyebody.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import com.example.android.eyebody.R
import java.util.*

/**
 * Created by YOON on 2017-10-07.
 */
class TargetDatePickDialog : DialogFragment() {
    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflater = activity.layoutInflater
        val dialogbuilder = AlertDialog.Builder(activity)
        val view = layoutInflater.inflate(R.layout.dialog_init3_date_pick, null)

        val datePicker = view.findViewById<DatePicker>(R.id.datePicker_dialog_init3_date_pick)
        val button_cancel = view.findViewById<Button>(R.id.button_dialog_init3_date_cancel)
        val button_submit = view.findViewById<Button>(R.id.button_dialog_init3_date_submit)

        val currentDay = Calendar.getInstance().time
        datePicker.updateDate(currentDay.year+1900,currentDay.month,currentDay.day)


        button_cancel.setOnClickListener{
            dismiss()
        }
        button_submit.setOnClickListener {
            val textviewDate = activity.findViewById<View>(android.R.id.content).findViewById<TextView>(R.id.TextView_target_date)
            textviewDate.text = "${datePicker.year}년 ${datePicker.month}월 ${datePicker.dayOfMonth}일"
            dismiss()
        }

        return dialogbuilder.create()
    }
}