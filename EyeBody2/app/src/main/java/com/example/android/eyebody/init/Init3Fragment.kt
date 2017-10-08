package com.example.android.eyebody.init

import android.annotation.SuppressLint
import android.os.Bundle
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.android.eyebody.MainActivity
import com.example.android.eyebody.R
import com.example.android.eyebody.dialog.TargetDatePickDialog
import com.example.android.eyebody.dialog.TargetWeightPickDialog

/**
 * Created by YOON on 2017-09-24.
 */

// target 설정 (일단은 목표 몸무게, 날짜)
// progress dot (3/3)

class Init3Fragment : Fragment() {

    @SuppressLint("ApplySharedPref")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)

        val v = inflater!!.inflate(R.layout.fragment_init3, container, false)
        Log.d("mydbg_init3", "[ init3 진입 ]")

        val textview_targetWeight = v.findViewById<TextView>(R.id.TextView_target_weight)
        val textview_targetDate = v.findViewById<TextView>(R.id.TextView_target_date)
        val button_callWeightDialog = v.findViewById<Button>(R.id.Button_call_weight_dialog)
        val button_callDateDialog = v.findViewById<Button>(R.id.Button_call_date_dialog)
        val button_submit = v.findViewById<Button>(R.id.Button_submit_target)

        var date: String
        var weight: Int? = 0


        button_callWeightDialog.setOnClickListener {
            val weightPicker = TargetWeightPickDialog()
            weightPicker.show(activity.fragmentManager, "pick target weight")
        }
        button_callDateDialog.setOnClickListener {
            val datePicker = TargetDatePickDialog()
            datePicker.show(fragmentManager.beginTransaction(), "pick target date")
        }
        button_submit.setOnClickListener {

            weight = textview_targetWeight.text.toString().toIntOrNull()
            if (weight == null) weight = resources.getInteger(R.integer.sharedPreference_default_targetWeight)
            date = textview_targetDate.text.toString()

            if (weight != resources.getInteger(R.integer.sharedPreference_default_targetWeight) &&
                    date != getString(R.string.textview_target2)) {

                activity.getSharedPreferences(getString(R.string.sharedPreference_initSetting), Context.MODE_PRIVATE)
                        .edit()
                        .putInt(getString(R.string.sharedPreference_targetWeight), weight!!)
                        .putString(getString(R.string.sharedPreference_targetDate), date)
                        .commit()

                val goMain = Intent(activity, MainActivity::class.java)
                startActivity(goMain)
                activity.finish()
                Log.d("mydbg_init3", "설정 된 값은\ntargetWeight = $weight\ntargetDate = $date")

            } else {
                Toast.makeText(activity, "두 개다 작성 해주셔야 돼요", Toast.LENGTH_LONG).show()
            }

        }


        return v
    }

}