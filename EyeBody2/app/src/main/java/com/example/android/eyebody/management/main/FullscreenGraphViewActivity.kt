package com.example.android.eyebody.management.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.android.eyebody.R
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_fullscreen_graphview.*
import java.util.*

/**
 * Created by YOON on 2017-12-10
 */
class FullscreenGraphViewActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen_graphview)

        val item = intent.extras.get("item") as MainManagementContent

        val contentGraph = graphView

        val arr = arrayListOf<DataPoint>()
        val calendar = Calendar.getInstance()
        for (i in item.dateDataList){
            if(i.date.length == 8){
                val xYear = i.date.substring(0,4).toIntOrNull()
                val xMonth = i.date.substring(4,6).toIntOrNull()
                val xDay = i.date.substring(6,8).toIntOrNull()
                val y = i.weight
                val uri = i.imageUri
                if(xYear != null && xMonth != null && xDay != null && y!=null) {
                    calendar.set(xYear, xMonth, xDay)
                    arr.add(DataPoint(calendar.time,y))
                }
            }
        }
        val series = LineGraphSeries<DataPoint>(arr.toTypedArray())
        contentGraph.addSeries(series)

        contentGraph.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(this)
        contentGraph.gridLabelRenderer.numHorizontalLabels = 4

        calendar.set(2017,12,23) //이전날짜
        contentGraph.viewport.setMinX(calendar.time.time.toDouble())
        calendar.set(2018, 1, 23) //도달날짜.
        contentGraph.viewport.setMaxX(calendar.time.time.toDouble())
        contentGraph.viewport.setMinY(30.0) //최소에서 +10으로 바까야함
        contentGraph.viewport.setMaxY(100.0) //최대에서
        contentGraph.viewport.isXAxisBoundsManual = true
        contentGraph.viewport.isYAxisBoundsManual = true

        contentGraph.gridLabelRenderer.isHumanRounding = false
    }
}