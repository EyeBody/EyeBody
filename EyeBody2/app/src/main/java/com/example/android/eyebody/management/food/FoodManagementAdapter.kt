package com.example.android.eyebody.management.food

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.example.android.eyebody.R
import com.example.android.eyebody.management.BasePageAdapter

/**
 * Created by YOON on 2017-11-19
 */
class FoodManagementAdapter(context: Context, contents: ArrayList<FoodManagementContent>) : BasePageAdapter(context, contents as ArrayList<Any>) {
    val dbHelper = DbHelper(context, "bill.db", null, 1)
    override fun getItem(position: Int):FoodManagementContent = contents[position] as FoodManagementContent

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val mView = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_food_management, parent, false)

        val date: TextView = mView.findViewById(R.id.textview_date_list_food_management)
        //val simpleText : TextView = mView.findViewById(R.id.textview_simplecontent_list_food_management)
        val constraintExpandableContent: ConstraintLayout = mView.findViewById(R.id.constraint_expandable_content_list_food_management)


        //date.text = dbHelper.getResult()

        val item = getItem(position)
        date.text = "날짜 : ${item.date}\n" +
                "메뉴 : ${item.menu}\n" +
                "가격 : ${item.price}"

        return mView
    }
}