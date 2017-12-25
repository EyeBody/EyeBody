package com.example.android.eyebody.management.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.example.android.eyebody.R
import com.example.android.eyebody.management.BasePageAdapter
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import java.util.*
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import org.apache.commons.math3.analysis.MultivariateFunction
import org.apache.commons.math3.analysis.function.Abs
import org.apache.commons.math3.analysis.function.Exp
import org.apache.commons.math3.analysis.function.Pow
import org.apache.commons.math3.analysis.function.Power
import org.apache.commons.math3.optim.InitialGuess
import org.apache.commons.math3.optim.MaxEval
import org.apache.commons.math3.optim.PointValuePair
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexOptimizer
import java.sql.Date
import java.util.function.LongToDoubleFunction


/**
 * Created by YOON on 2017-11-12
 *
 * graph opensource at
 * https://github.com/appsthatmatter/GraphView
 */
class MainManagementAdapter(context: Context, contents: ArrayList<MainManagementContent>) : BasePageAdapter(context, contents as ArrayList<Any>) {

    companion object {
        fun stringToCalendar(date: String): Calendar? {
            val calendar = Calendar.getInstance()
            val year = date.substring(0, 4).toIntOrNull()
            val month = date.substring(4, 6).toIntOrNull()
            val day = date.substring(6, 8).toIntOrNull()
            return if (year != null && month != null && day != null) {
                calendar.set(year, month - 1, day)
                calendar
            } else
                null
        }
    }

    override fun getItem(position: Int): MainManagementContent = contents[position] as MainManagementContent

    @SuppressLint("SetTextI18n")
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
        val contentDeleteButton: ImageButton = view.findViewById(R.id.imageButton)
        val contentGoToGalleryButton: ImageButton = view.findViewById(R.id.imageButton2)

        val item = getItem(position)

        contentDate.text = item.startDate + "~" + item.desireDate + " (desire : ${item.desireWeight} kg)"

        if (item.isInProgress) {
            contentTitleText.text = "진행중인 목표"
            contentTitleText.setTextColor(Color.BLACK)
            contentTitleBackground.background = ColorDrawable(Color.LTGRAY)
            contentDeleteButton.setOnClickListener {
                val result = MainManagementContent.terminateProgress(context, item)
                if (result == MainManagementContent.MAIN_MANAGEMENT_SUCCESS) {
                    notifyDataSetChanged()
                } else {
                    Log.d(TAG, "progress terminate Failed : $result")
                }
            }
        } else {
            contentTitleText.text = "종료된 목표"
            contentTitleText.setTextColor(Color.WHITE)
            contentTitleBackground.background = ColorDrawable(Color.DKGRAY)
            contentDeleteButton.setOnClickListener {
                // 데이터상에서 삭제
                MainManagementContent.deleteMainManagementContent(context, position)
                // 다시 불러와야 하지만 성능을 위해 화면에서만 지움.
                contents.removeAt(position)

                val xx = contentBackground.x
                contentBackground.animate()
                        ?.x(-contentBackground.width.toFloat())
                        ?.setDuration(350)
                        ?.setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                super.onAnimationEnd(animation)
                                contentBackground.x = xx
                                notifyDataSetChanged()
                            }
                        })
            }
        }


        val weightArray = arrayListOf<DataPoint>()
        var minWeight = 200.0
        var maxWeight = 0.0

        for (i in item.dateDataList) {
            if (i.date.length == 8) {
                val calendar = stringToCalendar(i.date)
                val yWeight = i.weight
                val uri = i.imageUri
                if (calendar != null && yWeight != null) {
                    if (i.weight!! < minWeight)
                        minWeight = i.weight!!
                    if (i.weight!! > maxWeight)
                        maxWeight = i.weight!!
                    weightArray.add(DataPoint(calendar.time, yWeight))
                }
            }
        }

        var calendar = Calendar.getInstance()

        val weightSeries = LineGraphSeries<DataPoint>(weightArray.toTypedArray())
        val foodSeries = BarGraphSeries<DataPoint>(arrayOf()/*TODO DB에서 가져옴*/)

        weightSeries.isDrawDataPoints = true

        contentGraph.removeAllSeries()
        contentGraph.addSeries(weightSeries)
        //contentGraph.addSeries(foodSeries)

        val arr = arrayOf(DataPoint(calendar.time, 300.0))

        if (item.isInProgress) {
            calendar = Calendar.getInstance()
            val todayIndicatorSeries = LineGraphSeries<DataPoint>(arrayOf(DataPoint(calendar.time, 0.0)))
            calendar.add(Calendar.SECOND, 1)
            todayIndicatorSeries.appendData(DataPoint(calendar.time, 200.0), false, 3)

            //todayIndicatorSeries.color = Color.RED
            val paint = Paint()
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 5f
            paint.pathEffect = DashPathEffect(floatArrayOf(5f, 3f), 0f)
            paint.color = Color.RED

            todayIndicatorSeries.isDrawAsPath = true
            todayIndicatorSeries.setCustomPaint(paint)
            contentGraph.addSeries(todayIndicatorSeries)

            /*val nowDaySeries = BarGraphSeries<DataPoint>(a)
            nowDaySeries.color = Color.RED
            contentGraph.addSeries(nowDaySeries)
            nowDaySeries.spacing = 1000*/


            // --------------------------- estimate ===================================================================================================================
            /*
             (비선형 회귀)
             오차함수 = y-(c/(1+e^(ax+b)))+d

             (추측값)
             초기값 : a = 0.01
                      b = -0.01 * 첫 몸무게
                      c = 몸무게첫날,현재날 차이 * 1/기간진행률 * 2
                      d = 첫값 - c/2

             nelder-mead simplex 알고리즘 사용.
             */

            calendar = Calendar.getInstance()
            var wholeProgress = stringToCalendar(item.desireDate)!!.time.time - stringToCalendar(item.startDate)!!.time.time
            var nowProgress = calendar.time.time - stringToCalendar(item.startDate)!!.time.time
            if (nowProgress == 0L) nowProgress = 1
            if (wholeProgress == 0L) wholeProgress = 1

            val realXY = arrayOf(Pair(1, 80.0), Pair(2, 79.0), Pair(3, 78.7), Pair(4, 79.0), Pair(5, 76.2))
            val guessA = 0.01
            val guessB = -0.01 * item.startWeight
            val guessC = (maxWeight - minWeight) / (wholeProgress/nowProgress) * 2
            val guessD = item.startWeight - guessC / 2

            class MyErrorFunction : MultivariateFunction {
                fun realFunction(x: Int, point: DoubleArray): Double {
                    val a = point[0]
                    val b = point[1]
                    val c = point[2]
                    val d = point[3]
                    return (c / (1 + Exp().value(a * x + b))) + d
                }

                override fun value(point: DoubleArray?): Double {
                    point!!
                    val a = point[0]
                    val b = point[1]
                    val c = point[2]
                    val d = point[3]
                    var sum = 0.0
                    for (xy in realXY) {
                        val x = xy.first.toDouble()
                        val y = xy.second
                        sum += Pow().value((y - (c / (1 + Exp().value(a * x + b)))), 2.0)
                    }
                    return sum
                }
            }

            val optimum = SimplexOptimizer(1e-1, 1e-3)
                    .optimize(
                            MaxEval(100000000),
                            ObjectiveFunction(MyErrorFunction()),
                            GoalType.MINIMIZE,
                            InitialGuess(
                                    doubleArrayOf(guessA, guessB, guessC, guessD)
                            ),
                            NelderMeadSimplex(4)
                    )
            val ref = optimum.point
            for (i in 1 until 10) {
                Log.d(TAG, "$i : ${MyErrorFunction().realFunction(i, ref)} when ${ref[0]} ${ref[1]} ${ref[2]} ${ref[3]}")
            }


            val estimateWeightArray = arrayListOf<DataPoint>()
            var count = 1
            calendar = stringToCalendar(item.startDate)
            val desireCal = stringToCalendar(item.desireDate)!!
            while (calendar.timeInMillis <= desireCal.timeInMillis) {
                estimateWeightArray.add(DataPoint(calendar.time, MyErrorFunction().realFunction(count, ref)))
                calendar.add(Calendar.DATE, 1)
                count++
            }
            val weightEstimateSeries = LineGraphSeries<DataPoint>(estimateWeightArray.toTypedArray())
            weightEstimateSeries.color = Color.YELLOW
            contentGraph.addSeries(weightEstimateSeries)

            // --------------------------- estimate ===================================================================================================================

        }
        contentGoToGalleryButton.setOnClickListener {
            //for the test
            MainManagementContent.insertMainManagementContent(context, MainManagementContent(
                    true, "20171220", "20171231", 80.0, 70.0,
                    arrayListOf()))
            MainManagementContent.addDateDataToProgressContent(context, "20171220")
            MainManagementContent.setWeightToProgressContent(context, "20171220", 80.0)
            MainManagementContent.addDateDataToProgressContent(context, "20171221")
            MainManagementContent.setWeightToProgressContent(context, "20171221", 79.0)
            MainManagementContent.addDateDataToProgressContent(context, "20171222")
            MainManagementContent.setWeightToProgressContent(context, "20171222", 77.2)
            MainManagementContent.addDateDataToProgressContent(context, "20171223")
            MainManagementContent.setWeightToProgressContent(context, "20171223", 78.0)
            MainManagementContent.addDateDataToProgressContent(context, "20171225")
            MainManagementContent.setWeightToProgressContent(context, "20171225", 76.0)
            notifyDataSetChanged()
            //for the test
        }

        contentGraph.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(context)
        contentGraph.gridLabelRenderer.numHorizontalLabels = 4


        val startTime = stringToCalendar(item.startDate)
        startTime?.add(Calendar.HOUR, -6)
        contentGraph.viewport.setMinX(startTime?.time?.time?.toDouble() ?: -10.0)
        val endTime = stringToCalendar(item.desireDate)
        startTime?.add(Calendar.HOUR, +1)
        contentGraph.viewport.setMaxX(endTime?.time?.time?.toDouble() ?: 10.0)
        if (minWeight < maxWeight) {
            maxWeight = (maxWeight.toInt() - maxWeight.toInt() % 10).toDouble() + 10
            minWeight = (minWeight.toInt() - minWeight.toInt() % 10).toDouble() - 10
        } else {
            minWeight = 30.0
            maxWeight = 100.0
        }
        contentGraph.viewport.setMinY(minWeight)
        contentGraph.viewport.setMaxY(maxWeight)
        contentGraph.viewport.isXAxisBoundsManual = true
        contentGraph.viewport.isYAxisBoundsManual = true

        contentGraph.gridLabelRenderer.isHumanRounding = false

        contentGraph.setOnClickListener {
            val mInt = Intent(context, FullscreenGraphViewActivity::class.java)
            mInt.putExtra("item", item)
            context.startActivity(mInt)
        }

        /*
            // 버튼 제어
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
            */

        return view
    }
}
