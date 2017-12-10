package com.example.android.eyebody.management.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.android.eyebody.R
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_fullscreen_graphview.*

/**
 * Created by YOON on 2017-12-10
 */
class FullscreenGraphViewActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen_graphview)

        val series = LineGraphSeries<DataPoint>(arrayOf(
                DataPoint(0.0, 1.0),
                DataPoint(1.0, 5.0),
                DataPoint(2.0, 3.0),
                DataPoint(3.0, 2.0),
                DataPoint(4.0, 6.0)
        ))
        graphView.addSeries(series)
        series.setAnimated(true)
    }
}