package com.example.android.eyebody.management.config

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.support.constraint.ConstraintLayout
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

class ConfigManagementSubAdapter(val context: Context, private val contents: Array<ConfigManagementSubContent>) : BaseAdapter() {

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

        if (contents[p].hasSwitch) {
            switch.visibility = View.VISIBLE
            switch.isChecked = (prefValue != 0)
            textConstraint.setOnClickListener {
                switch.isChecked = !switch.isChecked
            }
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
            // TODO : Dialog open and set shared preference value
            // preferenceName 이 null일 수 있음
        }

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
