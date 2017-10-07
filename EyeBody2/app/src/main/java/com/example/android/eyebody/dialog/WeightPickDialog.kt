package com.example.android.eyebody.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import com.example.android.eyebody.R

/**
 * Created by YOON on 2017-10-07.
 */
class WeightPickDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val layoutInflater = activity.layoutInflater
        val dialogbuilder = AlertDialog.Builder(activity)
        val view = layoutInflater.inflate(R.layout.dialog_init3_weight_pick, null)

        val numberPicker = view.findViewById<NumberPicker>(R.id.numberPicker_dialog_init3_weight_pick)
        val button_cancel = view.findViewById<Button>(R.id.button_dialog_init3_weight_cancel)
        val button_submit = view.findViewById<Button>(R.id.button_dialog_init3_weight_submit)

        numberPicker.minValue = 30
        numberPicker.maxValue = 200


        button_cancel.setOnClickListener{
            dismiss()
        }
        button_submit.setOnClickListener {
            val textviewWeight = activity.findViewById<View>(android.R.id.content).findViewById<TextView>(R.id.TextView_target_weight)
            textviewWeight.text = numberPicker.value.toString()
            dismiss()
        }

        return dialogbuilder.create()
    }
}