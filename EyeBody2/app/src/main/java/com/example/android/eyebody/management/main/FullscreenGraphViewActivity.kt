package com.example.android.eyebody.management.main

import android.content.Intent
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.android.eyebody.R
import com.example.android.eyebody.management.main.MainManagementAdapter.Companion.stringToCalendar
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_fullscreen_graphview.*
import org.apache.commons.math3.analysis.MultivariateFunction
import org.apache.commons.math3.analysis.function.Abs
import org.apache.commons.math3.analysis.function.Exp
import org.apache.commons.math3.optim.InitialGuess
import org.apache.commons.math3.optim.MaxEval
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexOptimizer
import java.util.*

/**
 * Created by YOON on 2017-12-10
 */
class FullscreenGraphViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen_graphview)

        val item = intent.extras.get("item") as MainManagementContent

        val contentGraph = graphView


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
            val wholeProgress = stringToCalendar(item.desireDate)!!.time.time - stringToCalendar(item.startDate)!!.time.time
            val nowProgress = calendar.time.time - stringToCalendar(item.startDate)!!.time.time

            val realXY = arrayOf(Pair(1, 80.0), Pair(2, 79.0), Pair(3, 78.7), Pair(4, 79.0), Pair(5, 76.2))
            val guessA = 0.01
            val guessB = -0.01 * item.startWeight
            val guessC = (maxWeight - minWeight) / (wholeProgress / nowProgress) * 2
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
                        sum += /*Pow().value((y - (c / (1 + Exp().value(a * x + b)))/*+d*/), 2.0)*/ Abs().value((y - (c / (1 + Exp().value(a * x + b))) + d))
                    }
                    return sum
                }
            }

            val optimum = SimplexOptimizer(1e-3, 1e-7)
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
            /*for(i in 1 until 10){
                Log.d(TAG, "$i : ${MyErrorFunction().realFunction(i, ref)} when ${ref[0]} ${ref[1]} ${ref[2]} ${ref[3]}")
            }*/


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
            weightEstimateSeries.color = Color.BLACK
            contentGraph.addSeries(weightEstimateSeries)

            // --------------------------- estimate ===================================================================================================================

        }


        contentGraph.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(this)
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

    }
}