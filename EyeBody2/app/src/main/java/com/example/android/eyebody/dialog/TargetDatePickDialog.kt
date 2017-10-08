package com.example.android.eyebody.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.util.Log
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
        val view = activity.layoutInflater.inflate(R.layout.dialog_init3_date_pick, null)
        val dialogbuilder = AlertDialog.Builder(activity).setView(view)

        Log.d("mydbg_datePicker", "[ datePicker 진입 ]")

        val datePicker = view.findViewById<DatePicker>(R.id.datePicker_dialog_init3_date_pick)
        val button_cancel = view.findViewById<Button>(R.id.button_dialog_init3_date_cancel)
        val button_submit = view.findViewById<Button>(R.id.button_dialog_init3_date_submit)

        val currentDay = Calendar.getInstance().time
        datePicker.updateDate(currentDay.year+1900,currentDay.month,currentDay.date)
        Log.d("mydbg_datePicker","time is ${Calendar.getInstance().time}\n${currentDay.year+1900}\t${currentDay.month}\t${currentDay.date}")

        button_cancel.setOnClickListener{
            Log.d("mydbg_datePicker","cancel button click")
            dismiss()
        }
        button_submit.setOnClickListener {
            val textviewDate = activity.findViewById<View>(android.R.id.content).findViewById<TextView>(R.id.TextView_target_date)
            textviewDate.text = "${datePicker.year}${String.format("%02d",datePicker.month+1)}${String.format("%02d",datePicker.dayOfMonth)}"
            Log.d("mydbg_datePicker","submit button click")
            dismiss()
        }

        return dialogbuilder.create()
    }


    override fun onStop() {
        super.onStop()
        Log.d("mydbg_datePicker", "다이얼로그가 가려짐 또는 종료됨")
    }
}