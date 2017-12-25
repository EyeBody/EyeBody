package com.example.android.eyebody.gallery

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Toast
import com.example.android.eyebody.R
import kotlinx.android.synthetic.main.activity_photo_frame.*
import java.lang.Math.abs

class PhotoFrameActivity : AppCompatActivity() {


    var photoList: ArrayList<Photo> = ArrayList<Photo>()
    var pos: Int = 0

    var x1: Float = 0f
    var x2: Float = 0f
    var minDistance: Int = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_frame)

        photoList = intent.getParcelableArrayListExtra("photoList")
        pos = intent.getIntExtra("pos", 0)

        //RecyclerView
        photoFrameRecyclerView.hasFixedSize()
        photoFrameRecyclerView.adapter = PhotoFrameAdapter(this, photoList)
        photoFrameRecyclerView.scrollToPosition(pos)

        hideActionBar()

        photoFrameRecyclerView.setOnTouchListener { view, motionEvent ->
            when (motionEvent.getAction()){
                MotionEvent.ACTION_DOWN -> {
                    x1 = motionEvent.x

                }
                MotionEvent.ACTION_UP -> {
                    x2 = motionEvent.x

                    //왼쪽에서 오른쪽으로 스와이프 -->
                    if (x2 - x1 > minDistance && pos - 1 >= 0) {
                        pos = pos - 1
                    }

                    //오른쪽에서 왼쪽으로 스와이프 <--
                    else if (x1 - x2 > minDistance && pos + 1 < photoList.size) {
                        pos = pos + 1
                    }

                    //짧은 터치
                    else if(abs(x1 - x2) < 50) {

                    }

                    photoFrameRecyclerView.smoothScrollToPosition(pos)
                }
            }
            false
        }

    }

    fun hideActionBar() {
        var actionBar = supportActionBar

        if (actionBar != null) {
            actionBar.hide()
        }
    }

}