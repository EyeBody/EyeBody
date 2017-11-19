package com.example.android.eyebody.management

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import com.example.android.eyebody.R

/**
 * Created by Yoon on 2017-11-18
 */
class ConfigManagementAdapter(val context: Context, private val contents: Array<ConfigManagementContent>) : BaseAdapter() {

    override fun getView(p: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_config_management, parent, false)

        val icon: ImageView = view.findViewById(R.id.icon_list_config_management)
        val mainText: TextView = view.findViewById(R.id.main_textview_list_config_management)
        val subText: TextView = view.findViewById(R.id.sub_textview_list_config_management)

        icon.setImageDrawable(contents[p].drawable)
        mainText.text = contents[p].text
        subText.text = contents[p].subText

        val subListView: ListView = view.findViewById(R.id.listview_sub_config_management)
        subListView.adapter = ConfigManagementSubAdapter(context, contents[p].subContent)
        //ArrayAdapter(context, android.R.layout.simple_list_item_1, arrayOf("aa","bb"))


        return view
    }

    override fun getItem(p: Int) = contents[p]

    override fun getItemId(p: Int) = p.toLong()

    override fun getCount() = contents.size
}


class ConfigManagementSubAdapter(val context: Context, private val contents: Array<ConfigManagementSubContent>) : BaseAdapter() {

    override fun getView(p: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_sub_listview_config_management, parent, false)

        val textConstraint: ConstraintLayout = view.findViewById(R.id.constraint_sub_item)
        val mainText: TextView = view.findViewById(R.id.main_textview_sub_listview_config_management)
        val subText: TextView = view.findViewById(R.id.sub_textview_sub_listview_config_management)
        val switch: Switch = view.findViewById(R.id.switch_sub_listview_config_management)

        val pref = context.getSharedPreferences(context.resources.getString(R.string.getSharedPreference_configuration), MODE_PRIVATE)
        val prefValue = pref.getInt(contents[p].preferenceName, 0)

        mainText.text = contents[p].text
        subText.text =
                if (contents[p].preferenceValueExplanation != null) {
                    contents[p].preferenceValueExplanation!![prefValue]
                } else if (subText.visibility == GONE) {
                    ""
                } else {
                    val dp = context.resources.displayMetrics.density
                    val halfSubTextSize = (subText.textSize / 2).toInt()
                    mainText.setPaddingRelative(
                            mainText.paddingStart,
                            mainText.paddingTop + halfSubTextSize,
                            mainText.paddingEnd,
                            mainText.paddingBottom + halfSubTextSize)
                    subText.visibility = GONE
                    ""
                }

        if (contents[p].hasSwitch) {
            switch.visibility = VISIBLE
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
            switch.visibility = GONE
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


class ConfigManagementContent(val drawable: Drawable, val text: String, val subText: String, val subContent: Array<ConfigManagementSubContent>)

/**
 * preference are saved false or true
 * actually it saved 0, 1, 2, 3, ~.
 *
 * switch used when need boolean type selector,
 * switch not used when need string type selector (using dialog)
 * @param preferenceValueExplanation optional param, {false, true} for explanation string when using switch, {preferenceValueList} for explanation when not using switch.
 */
open class ConfigManagementSubContent(val text: String = "", val hasSwitch: Boolean, val preferenceName: String?,
                                      val preferenceValueExplanation: List<String>? = null) {
    /*
    text가 큰 글씨로 써지고
    그 밑에 preferenceValueExplanation이 써짐 (null일 경우 안 씀)
    preferenceValueList는 switch를 사용하지 않을 때 (dialog를 띄울때) 만 사용함.

    TODO : Diaglog는 어떻게 처리할까?
     */
}

class SwitchableSubContent(text: String, preferenceName: String,
                           preferenceValueExplanation: List<String>? = null) :
        ConfigManagementSubContent(
                text = text,
                hasSwitch = true,
                preferenceName = preferenceName,
                preferenceValueExplanation = preferenceValueExplanation)

class DialogableSubContent(text: String, preferenceName: String?,
                           preferenceValueExplanation: List<String>? = null) :
        ConfigManagementSubContent(
                text = text,
                hasSwitch = false,
                preferenceName = preferenceName,
                preferenceValueExplanation = preferenceValueExplanation)
