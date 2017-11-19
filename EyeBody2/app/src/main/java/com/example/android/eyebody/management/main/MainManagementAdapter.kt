package com.example.android.eyebody.management.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.example.android.eyebody.R
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint


/**
 * Created by YOON on 2017-11-12
 *
 * graph opensource at
 * https://github.com/appsthatmatter/GraphView
 */
class MainManagementAdapter(val context: Context, val contents: Array<MainManagementContent>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // item을 가지고 어떻게 표시할 것이냐 -> layout에 있는 xml파일을 부른다음에 거기에 contents 아이템들을 넣고 반환
        // convertView = 재사용
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_main_management, parent, false)


        /* TODO : DB mapping here */
        val contentBackground: ConstraintLayout = view.findViewById(R.id.const_main_management_parent)
        val contentDate: TextView = view.findViewById(R.id.textView3)
        val mainContentBackground: ConstraintLayout = view.findViewById(R.id.constraint_content_list_food_management)
        val contentTitleBackground: ConstraintLayout = view.findViewById(R.id.constraintLayout6)
        val contentTitleText: TextView = view.findViewById(R.id.textView4)
        val contentTitleImageWeight: ImageButton = view.findViewById(R.id.imageButton3)
        val contentTitleImageFood: ImageButton = view.findViewById(R.id.imageButton4)
        val contentGraph: GraphView = view.findViewById(R.id.main_management_graph)
        val contentDeleteOrFail: ImageButton = view.findViewById(R.id.imageButton)
        val contentGoToGallery: ImageButton = view.findViewById(R.id.imageButton2)


        val series = LineGraphSeries<DataPoint>( arrayOf<DataPoint>(
                        DataPoint(0.0, 1.0),
                        DataPoint(1.0, 5.0),
                        DataPoint(2.0, 3.0),
                        DataPoint(3.0, 2.0),
                        DataPoint(4.0, 6.0)
                ))
        contentGraph.addSeries(series)

        contentGraph.setOnClickListener {
            val buttonRange: ConstraintLayout? = parent?.rootView?.findViewById(R.id.constraintLayout7)

            if (buttonRange?.visibility == View.GONE) {
                buttonRange.animate()
                        ?.translationY(buttonRange.y)
                        ?.alpha(1.0f)
                        ?.setDuration(300)
                        ?.setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationStart(animation: Animator?) {
                                super.onAnimationStart(animation)
                                buttonRange.visibility = View.VISIBLE
                            }
                        })

            } else {
                buttonRange?.animate()
                        ?.translationY(-buttonRange.y)
                        ?.alpha(0.0f)
                        ?.setDuration(300)
                        ?.setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                super.onAnimationEnd(animation)
                                buttonRange.visibility = View.GONE
                            }
                        })
            }

        }

        return view
    }

    override fun getItem(position: Int): Any = contents[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = contents.size
}
