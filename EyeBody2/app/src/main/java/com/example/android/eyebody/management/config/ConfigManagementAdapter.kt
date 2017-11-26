package com.example.android.eyebody.management.config

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.support.constraint.ConstraintLayout
import android.util.Log
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
class ConfigManagementAdapter(val activity: Activity, private val contents: Array<ConfigManagementContent>) : BaseAdapter() {

    val context = activity.applicationContext!!

    val TAG = "mydbg_ConfigMngAdapter"

    override fun getView(p: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_config_management, parent, false)

        val icon: ImageView = view.findViewById(R.id.icon_list_config_management)
        val mainText: TextView = view.findViewById(R.id.main_textview_list_config_management)
        val subText: TextView = view.findViewById(R.id.sub_textview_list_config_management)

        icon.setImageDrawable(contents[p].drawable)
        mainText.text = contents[p].text
        subText.text = contents[p].subText

        val subListView: ListView = view.findViewById(R.id.listview_sub_config_management)
        subListView.adapter = ConfigManagementSubAdapter(activity, contents[p].subContent)

        return view
    }

    override fun getItem(p: Int) = contents[p]

    override fun getItemId(p: Int) = p.toLong()

    override fun getCount() = contents.size
}
