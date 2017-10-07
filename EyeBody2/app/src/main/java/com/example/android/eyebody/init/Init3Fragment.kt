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

/**
 * Created by YOON on 2017-09-24.
 */

// target 설정 (일단은 목표 몸무게, 날짜)
// progress dot (3/3)
// TODO(NOW!! : Init3 다 제대로 돌린 다음에 구글드라이브 연동 시작하기

class Init3Fragment : Fragment() {

    @SuppressLint("ApplySharedPref")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)

        val v = inflater!!.inflate(R.layout.fragment_init3, container, false)
        Log.d("mydbg_init3", "init3 진입")

        val textview_targetWeight = v.findViewById<TextView>(R.id.TextView_target_weight)
        val textview_targetDate = v.findViewById<TextView>(R.id.TextView_target_date)
        val button_callWeightDialog = v.findViewById<Button>(R.id.Button_call_weight_dialog)
        val button_callDateDialog = v.findViewById<Button>(R.id.Button_call_date_dialog)
        val button_submit = v.findViewById<Button>(R.id.Button_submit_target)

        var statusSetWeight = false
        var statusSetDate = false
        var date  = "yyyymmdd"
        var weight  = 0


        button_callWeightDialog.setOnClickListener {
            statusSetDate = true
            date = textview_targetDate.text.toString()
        }
        button_callDateDialog.setOnClickListener {
            statusSetWeight = true
            weight = textview_targetWeight.text.toString().toInt()
        }
        button_submit.setOnClickListener {

            if (statusSetDate && statusSetWeight) {

                activity.getSharedPreferences(getString(R.string.sharedPreference_initSetting), Context.MODE_PRIVATE)
                        .edit()
                        .putString(getString(R.string.sharedPreference_targetDate),date)
                        .putInt(getString(R.string.sharedPreference_targetWeight),weight)
                        .commit()

                val goMain = Intent(activity, MainActivity::class.java)
                startActivity(goMain)
                activity.finish()

            } else {
                Toast.makeText(activity,"두 개다 작성 해주셔야 돼요",Toast.LENGTH_LONG).show()
            }

        }


        return v
    }

}