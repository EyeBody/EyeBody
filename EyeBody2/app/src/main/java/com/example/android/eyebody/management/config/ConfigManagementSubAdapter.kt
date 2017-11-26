package com.example.android.eyebody.management.config

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Switch
import android.widget.TextView
import com.example.android.eyebody.R

/**
 * Created by YOON on 2017-11-19
 */

class ConfigManagementSubAdapter(val activity: Activity, private val contents: Array<ConfigManagementSubContent>) : BaseAdapter() {

    val TAG = "mydbg_ConfigMNG_SubAdap"
    val context = activity.applicationContext!!

    override fun getView(p: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_sub_listview_config_management, parent, false)

        val textConstraint: ConstraintLayout = view.findViewById(R.id.constraint_sub_item)
        val mainText: TextView = view.findViewById(R.id.main_textview_sub_listview_config_management)
        val subText: TextView = view.findViewById(R.id.sub_textview_sub_listview_config_management)
        val switch: Switch = view.findViewById(R.id.switch_sub_listview_config_management)

        val pref = context.getSharedPreferences(context.resources.getString(R.string.getSharedPreference_configuration), Context.MODE_PRIVATE)
        val prefValue = pref.getInt(contents[p].preferenceName, 0)

        mainText.text = contents[p].text
        subText.text =
                when {
                    contents[p].preferenceValueExplanation != null ->
                        contents[p].preferenceValueExplanation!![prefValue]

                    subText.visibility == View.GONE ->
                        ""

                    else -> {
                        val dp = context.resources.displayMetrics.density
                        val halfSubTextSize = (subText.textSize / 2).toInt()
                        mainText.setPaddingRelative(mainText.paddingStart, mainText.paddingTop + halfSubTextSize, mainText.paddingEnd, mainText.paddingBottom + halfSubTextSize)
                        subText.visibility = View.GONE
                        ""
                    }
                }


        val clickToCallOrNoneAutoListener = View.OnClickListener { v->
            // TODO 클릭했을 때 listview의 item click이 작동해야 함.

            fun justToggleOrValueUp() {
                Log.d(TAG, "${(pref.getInt(contents[p].preferenceName, 0))} -> ${(pref.getInt(contents[p].preferenceName, 0) + 1).rem(contents[p].preferenceValueExplanation?.size ?: 1)}")
                if (switch.visibility == View.VISIBLE) {
                    if(v !is Switch)
                        switch.isChecked = !switch.isChecked
                    // switch changed listener 를 통해서 value를 저장함.
                } else {
                    // value++
                    if (contents[p].preferenceName != null) {
                        savePreference(pref, contents[p].preferenceName!!,
                                (pref.getInt(contents[p].preferenceName, 0) + 1).rem(contents[p].preferenceValueExplanation?.size ?: 1)
                        )
                        notifyDataSetChanged()
                    }
                }
            }

            when (contents[p]) {

                is CallableSubContent -> {

                    val subContent = (contents[p] as CallableSubContent)
                    val prefCurrentStatus = pref.getInt(contents[p].preferenceName, 0)

                    if (subContent.canCall(prefCurrentStatus)) {
                        // 현재 상태에서 정해진 dialog가 존재한다면 스위치 여부를 dialog에 물어보고 토글
                        val result = 0
                        // call이 fragment를 콜하는지 activity를 콜하는지 명시해줄 필요 있나..?
                        subContent.call(activity, prefCurrentStatus)
                        if (subContent is DialogCallerSubContent) {
                            // TODO : fragment호출 후 switch 변동 주어야 함.
                            if (result == 1) { // 물어봐서 된다고 했음.
                                switch.isChecked = !switch.isChecked //TODO 다른데서 정의해줘야 하는 부분.
                            }
                        } else if (subContent is ActivityCallerSubContent) {
                            // TODO : 다른 곳에서 forResult를 이용해서 switch 변동을 시켜주어야 함.
                        }
                    } else {
                        // 정해진 dialog가 없다면 스위치 토글 또는 value ++
                        justToggleOrValueUp()
                    }
                }

            // Callable이 아닌 경우에도 스위치 토글 또는 value ++
                else -> justToggleOrValueUp()
            }
        }

        if (contents[p].hasSwitch) {
            switch.isChecked = (prefValue != 0) //isChecked = prefValue != false
            switch.setOnClickListener(clickToCallOrNoneAutoListener)
            switch.setOnCheckedChangeListener { compoundButton, b ->
                savePreference(pref, contents[p].preferenceName!!,
                        if (switch.isChecked)
                            1
                        else
                            0
                )
                notifyDataSetChanged()
            }
        } else {
            switch.visibility = View.GONE
        }
        textConstraint.setOnClickListener(clickToCallOrNoneAutoListener)


        return view
    }

    override fun getItem(p: Int) = contents[p]

    override fun getItemId(p: Int) = p.toLong()

    override fun getCount() = contents.size

    @SuppressLint("ApplySharedPref")
    private fun savePreference(pref: SharedPreferences, name: String, value: Int) {
        pref.edit().putInt(name, value).commit()
    }
}
