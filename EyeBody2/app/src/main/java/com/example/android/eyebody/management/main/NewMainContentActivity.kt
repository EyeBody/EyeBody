package com.example.android.eyebody.management.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.NumberPicker
import android.widget.Toast
import com.example.android.eyebody.R
import com.example.android.eyebody.management.ManagementActivity
import kotlinx.android.synthetic.main.activity_new_main_content.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by YOON on 2017-12-09
 */
class NewMainContentActivity : AppCompatActivity() {

    val TAG = "mydbg_NewMainContent"

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_main_content)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val nowDate = GregorianCalendar()
        val nowYear = nowDate.get(GregorianCalendar.YEAR)
        val nowMonth = nowDate.get(GregorianCalendar.MONTH)+1
        val nowDay = nowDate.get(GregorianCalendar.DATE)

        numberpicker_start_year.minValue = nowYear
        numberpicker_start_year.maxValue = nowYear
        numberpicker_start_year.value = nowYear
        numberpicker_start_year.wrapSelectorWheel = false

        numberpicker_start_month.minValue = nowMonth
        numberpicker_start_month.maxValue = nowMonth
        numberpicker_start_month.value = nowMonth

        numberpicker_start_day.minValue = nowDay
        numberpicker_start_day.maxValue = nowDay
        numberpicker_start_day.value = nowDay


        numberpicker_end_year.minValue = nowYear
        numberpicker_end_year.maxValue = nowYear+10
        nowDate.add(GregorianCalendar.MONTH, 1)
        numberpicker_end_year.value = nowDate.get(GregorianCalendar.YEAR)
        numberpicker_end_year.wrapSelectorWheel = false

        numberpicker_end_month.minValue = 1
        numberpicker_end_month.maxValue = 12
        numberpicker_end_month.value = nowDate.get(GregorianCalendar.MONTH)+1

        numberpicker_end_day.minValue = 1
        numberpicker_end_day.maxValue = 31
        numberpicker_end_day.value = nowDate.get(GregorianCalendar.DATE)


        numberpicker_start_weight.minValue = 30
        numberpicker_start_weight.maxValue = 200
        numberpicker_start_weight.value = 75
        numberpicker_start_weight.wrapSelectorWheel = false

        numberpicker_end_weight.minValue = 30
        numberpicker_end_weight.maxValue = 200
        numberpicker_end_weight.value = 70
        numberpicker_end_weight.wrapSelectorWheel = false


        numberpicker_end_month.setOnValueChangedListener { numberPicker, oldVal, newVal ->
            when (newVal) {
                2 -> {
                    val year = numberpicker_end_year.value
                    if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
                        numberpicker_end_day.maxValue = 29
                    numberpicker_end_day.maxValue = 28
                }
                1, 3, 5, 7, 8, 10, 12 -> numberpicker_end_day.maxValue = 31
                else -> numberpicker_end_day.maxValue = 30
            }
            if (oldVal == numberPicker.maxValue && newVal == 1) {
                numberpicker_end_year.value += 1
            } else if (oldVal == 1 && newVal == numberPicker.maxValue) {
                numberpicker_end_year.value -= 1
            }
        }
        numberpicker_end_day.setOnValueChangedListener { numberPicker, oldVal, newVal ->
            if (oldVal == numberPicker.maxValue && newVal == 1) {
                numberpicker_end_month.value += 1
            } else if (oldVal == 1 && newVal == numberPicker.maxValue) {
                numberpicker_end_month.value -= 1
            }
        }

        fun weightChange(weightPicker: NumberPicker, change: Int) =
                when {
                    (weightPicker.value + change) > weightPicker.maxValue -> weightPicker.value = weightPicker.maxValue
                    (weightPicker.value + change) < weightPicker.minValue -> weightPicker.value = weightPicker.minValue
                    else -> weightPicker.value += change
                }

        button_start_weight_up.setOnClickListener { weightChange(numberpicker_start_weight, 10) }
        button_start_weight_double_up.setOnClickListener { weightChange(numberpicker_start_weight, 20) }
        button_start_weight_down.setOnClickListener { weightChange(numberpicker_start_weight, -10) }
        button_start_weight_double_down.setOnClickListener { weightChange(numberpicker_start_weight, -20) }

        button_end_weight_up.setOnClickListener { weightChange(numberpicker_end_weight, 10) }
        button_end_weight_double_up.setOnClickListener { weightChange(numberpicker_end_weight, 20) }
        button_end_weight_down.setOnClickListener { weightChange(numberpicker_end_weight, -10) }
        button_end_weight_double_down.setOnClickListener { weightChange(numberpicker_end_weight, -20) }

        button_submit.setOnClickListener {

            // 왜 month 저거 저딴식으로 만들어가지고 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            val startDate = GregorianCalendar(numberpicker_start_year.value, numberpicker_start_month.value-1, numberpicker_start_day.value)
            val endDate = GregorianCalendar(numberpicker_end_year.value, numberpicker_end_month.value-1, numberpicker_end_day.value)

            if (edittext_goal_name.text.isEmpty()) {
                Toast.makeText(this, "목표 이름을 설정해주세요.", Toast.LENGTH_SHORT).show()
            } else if (startDate >= endDate) {
                Toast.makeText(this, "종료 날짜가 시작 날짜보다 같거나 이를 수 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val sdf = SimpleDateFormat("yyyyMMdd")
                val startDateFormat = sdf.format(startDate.time)
                val endDateFormat = sdf.format(endDate.time)
                val content = MainManagementContent(
                        true,
                        startDateFormat,
                        endDateFormat,
                        numberpicker_start_weight.value.toDouble(), numberpicker_end_weight.value.toDouble(),
                        arrayListOf()
                )

                MainManagementContent.insertMainManagementContent(baseContext, content)

                val toMainWithActivityStackClear = Intent(applicationContext, ManagementActivity::class.java)
                toMainWithActivityStackClear.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(toMainWithActivityStackClear)
            }
        }
    }
}