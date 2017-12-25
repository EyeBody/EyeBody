package com.example.android.eyebody.gallery

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MotionEvent
import android.view.View
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

        hideActionBar()

        //RecyclerView
        photoFrameRecyclerView.hasFixedSize()
        photoFrameRecyclerView.adapter = PhotoFrameAdapter(this, photoList)
        photoFrameRecyclerView.scrollToPosition(pos)

        memoTextView.setMovementMethod(ScrollingMovementMethod())
        memoTextView.text = photoList[pos].getMemo()
        dateTextView.text = photoList[pos].getDate("yyyy년 dd월 mm일")

        editMemoButton.setOnClickListener {
            Toast.makeText(this, "메모 작성", Toast.LENGTH_SHORT).show()
        }

        photoFrameRecyclerView.setOnTouchListener { view, motionEvent ->
            when (motionEvent.getAction()){
                MotionEvent.ACTION_DOWN -> {
                    x1 = motionEvent.x
                }
                MotionEvent.ACTION_UP -> {
                    x2 = motionEvent.x

                    //스와이프
                    if(abs(x1 - x2) > minDistance){
                        if (x1 < x2 && pos - 1 >= 0)    //왼쪽에서 오른쪽으로 스와이프 -->
                            pos = pos - 1
                        else if (x1 > x2 && pos + 1 < photoList.size)   //오른쪽에서 왼쪽으로 스와이프 <--
                            pos = pos + 1

                        dateTextView.text = photoList[pos].getDate("yyyy년 dd월 mm일")
                        memoTextView.scrollTo(0, 0)
                        memoTextView.text = photoList[pos].getMemo()
                    }
                    //짧은 터치
                    else if (abs(x1 - x2) < 50) {
                        toggleMemo()
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

    fun toggleMemo(){
        if(memoTextView.visibility == View.VISIBLE){
            dateTextView.visibility = View.INVISIBLE
            memoTextView.visibility = View.INVISIBLE
            editMemoButton.visibility = View.INVISIBLE
        } else {
            dateTextView.visibility = View.VISIBLE
            memoTextView.visibility = View.VISIBLE
            editMemoButton.visibility = View.VISIBLE
        }
    }
}