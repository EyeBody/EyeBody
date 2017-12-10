package com.example.android.eyebody.management.config

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.android.eyebody.R
import com.example.android.eyebody.management.BasePageAdapter

/**
 * Created by Yoon on 2017-11-18
 */
class ConfigManagementAdapter(val activity: Activity, contents: ArrayList<ConfigManagementContent>) :
        BasePageAdapter(activity.baseContext, contents as ArrayList<Any>) {

    val TAG = "mydbg_ConfigMngAdapter"

    override fun getItem(position: Int): ConfigManagementContent = contents[position] as ConfigManagementContent

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_config_management, parent, false)

        val icon: ImageView = view.findViewById(R.id.icon_list_config_management)
        val mainText: TextView = view.findViewById(R.id.main_textview_list_config_management)
        val subText: TextView = view.findViewById(R.id.sub_textview_list_config_management)

        val item = getItem(position)
        icon.setImageDrawable(item.drawable)
        mainText.text = item.text
        subText.text = item.subText

        val subListView: ListView = view.findViewById(R.id.listview_sub_config_management)
        subListView.adapter = ConfigManagementSubAdapter(activity, item.subContent)

        return view
    }
}
