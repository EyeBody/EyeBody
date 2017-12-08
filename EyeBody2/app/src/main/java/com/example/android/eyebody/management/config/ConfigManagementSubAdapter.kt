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
import android.widget.Switch
import android.widget.TextView
import com.example.android.eyebody.R
import com.example.android.eyebody.management.BasePageAdapter
import com.example.android.eyebody.management.config.subcontent.CMBaseSubContent
import com.example.android.eyebody.management.config.subcontent.caller.CallableSubContent
import com.example.android.eyebody.management.config.subcontent.caller.DialogCallerSubContent
import com.example.android.eyebody.management.config.subcontent.caller.FunctionCallerSubContent

/**
 * Created by YOON on 2017-11-19
 */

class ConfigManagementSubAdapter(val activity: Activity, contents: Array<CMBaseSubContent>) :
        BasePageAdapter(activity.applicationContext, contents as Array<Any>) {

    val TAG = "mydbg_ConfigMNG_SubAdap"

    override fun getItem(position: Int): CMBaseSubContent = contents[position] as CMBaseSubContent

    @SuppressLint("ApplySharedPref")
    private fun savePreference(pref: SharedPreferences, name: String, value: Int) {
        pref.edit().putInt(name, value).commit()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_sub_listview_config_management, parent, false)

        val textConstraint: ConstraintLayout = view.findViewById(R.id.constraint_sub_item)
        val mainText: TextView = view.findViewById(R.id.main_textview_sub_listview_config_management)
        val subText: TextView = view.findViewById(R.id.sub_textview_sub_listview_config_management)
        val switch: Switch = view.findViewById(R.id.switch_sub_listview_config_management)

        val pref = context.getSharedPreferences(context.resources.getString(R.string.getSharedPreference_configuration_Only_Int), Context.MODE_PRIVATE)
        val content = getItem(position)
        val prefValue = pref.getInt(content.preferenceName, 0)

        mainText.text = content.text
        subText.text =
                when {
                    subText.visibility == View.GONE ->
                        ""
                    content.preferenceValueExplanation != null ->
                        content.preferenceValueExplanation[prefValue]
                    else -> {
                        // val dp = context.resources.displayMetrics.density
                        val halfSubTextSize = (subText.textSize / 2).toInt()
                        mainText.setPaddingRelative(
                                mainText.paddingStart,
                                mainText.paddingTop + halfSubTextSize,
                                mainText.paddingEnd,
                                mainText.paddingBottom + halfSubTextSize)
                        subText.visibility = View.GONE
                        ""
                    }
                }

        val clickToCallOrNoneAutoListener = View.OnClickListener { v ->
            fun justToggleOrValueUp() {
                Log.d(TAG, "${(pref.getInt(content.preferenceName, 0))}" +
                        " -> ${(pref.getInt(content.preferenceName, 0) + 1).rem(content.preferenceValueExplanation?.size ?: 1)}")
                if (switch.visibility == View.VISIBLE) {
                    if (v !is Switch)
                        switch.isChecked = !switch.isChecked
                    // switch changed listener 를 통해서 value를 저장함.
                } else {
                    // value++
                    if (content.preferenceName != null) {
                        savePreference(pref, content.preferenceName,
                                (pref.getInt(content.preferenceName, 0) + 1).rem(content.preferenceValueExplanation?.size ?: 1)
                        )
                        notifyDataSetChanged()
                    }
                }
            }

            when (content) {
                is CallableSubContent -> {
                    val prefCurrentStatus = pref.getInt(content.preferenceName, 0)
                    if (content.canCall(prefCurrentStatus)) {
                        // 현재 상태에서 정해진 dialog가 존재한다면 스위치 여부를 dialog에 물어보고 토글
                        if (content is DialogCallerSubContent) {
                            // 현재 서브컨텐츠의 상태값을 미리 기록했다가
                            content.storableDialog?.get(prefCurrentStatus)?.storedInt = prefCurrentStatus
                            // dismiss했을 때 그 값(또는 변경됐을 값)으로 sharedPreference에 저장하여 값을 수정한다.
                            content.storableDialog?.get(prefCurrentStatus)?.setOnDismissListener {
                                savePreference(pref, content.preferenceName!!, content.storableDialog[prefCurrentStatus]?.storedInt ?: prefCurrentStatus)
                                notifyDataSetChanged()
                            }
                        } else if (content is FunctionCallerSubContent) {
                            content.ret = prefCurrentStatus
                            content.setOnReturnListener {
                                savePreference(pref, content.preferenceName!!, content.ret ?: prefCurrentStatus)
                                notifyDataSetChanged()
                            }
                        }
                        content.call(activity, prefCurrentStatus) //activity, dialog, function will call.
                    } else {
                        // 정해진 dialog가 없다면 스위치 토글 또는 value ++
                        justToggleOrValueUp()
                    }
                }

            // Callable이 아닌 경우에도 스위치 토글 또는 value ++
                else -> justToggleOrValueUp()
            }
        }

        textConstraint.setOnClickListener(clickToCallOrNoneAutoListener)
        if (content.hasSwitch) {
            switch.isChecked = (prefValue != 0) //isChecked = prefValue != false
            switch.setOnClickListener(clickToCallOrNoneAutoListener)
            switch.setOnCheckedChangeListener { compoundButton, b ->
                savePreference(pref, content.preferenceName!!,
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

        return view
    }
}
