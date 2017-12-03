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
import com.example.android.eyebody.management.config.subcontent.CMBaseSubContent
import com.example.android.eyebody.management.config.subcontent.caller.CallableSubContent
import com.example.android.eyebody.management.config.subcontent.caller.DialogCallerSubContent
import com.example.android.eyebody.management.config.subcontent.caller.FunctionCallerSubContent

/**
 * Created by YOON on 2017-11-19
 */

class ConfigManagementSubAdapter(val activity: Activity, private val contents: Array<CMBaseSubContent>) : BaseAdapter() {

    val TAG = "mydbg_ConfigMNG_SubAdap"
    val context = activity.applicationContext!!

    override fun getView(p: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_sub_listview_config_management, parent, false)

        val textConstraint: ConstraintLayout = view.findViewById(R.id.constraint_sub_item)
        val mainText: TextView = view.findViewById(R.id.main_textview_sub_listview_config_management)
        val subText: TextView = view.findViewById(R.id.sub_textview_sub_listview_config_management)
        val switch: Switch = view.findViewById(R.id.switch_sub_listview_config_management)

        val pref = context.getSharedPreferences(context.resources.getString(R.string.getSharedPreference_configuration_Only_Int), Context.MODE_PRIVATE)
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


        val clickToCallOrNoneAutoListener = View.OnClickListener { v ->
            fun justToggleOrValueUp() {
                Log.d(TAG, "${(pref.getInt(contents[p].preferenceName, 0))} -> ${(pref.getInt(contents[p].preferenceName, 0) + 1).rem(contents[p].preferenceValueExplanation?.size ?: 1)}")
                if (switch.visibility == View.VISIBLE) {
                    if (v !is Switch)
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

                        if (subContent is DialogCallerSubContent) {
                            // 현재 서브컨텐츠의 상태값을 미리 기록했다가
                            subContent.storableDialog?.get(prefCurrentStatus)?.storedInt = prefCurrentStatus
                            // dismiss했을 때 그 값(또는 변경됐을 값)으로 sharedPreference에 저장하여 값을 수정한다.
                            subContent.storableDialog?.get(prefCurrentStatus)?.setOnDismissListener {
                                savePreference(pref, contents[p].preferenceName!!, subContent.storableDialog[prefCurrentStatus]?.storedInt ?: prefCurrentStatus)
                                notifyDataSetChanged()
                            }
                        } else if (subContent is FunctionCallerSubContent) {
                            subContent.ret = prefCurrentStatus
                            subContent.setOnReturnListener{
                                savePreference(pref, contents[p].preferenceName!!, subContent.ret ?: prefCurrentStatus)
                                notifyDataSetChanged()
                            }
                        }

                        subContent.call(activity, prefCurrentStatus) //activity, dialog, function will call.

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
