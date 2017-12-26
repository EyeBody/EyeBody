package com.example.android.eyebody.gallery

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.method.ScrollingMovementMethod
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.android.eyebody.R
import com.example.android.eyebody.camera.memoImageDb
import kotlinx.android.synthetic.main.activity_photo_frame.*
import java.lang.Math.abs

class PhotoFrameActivity : AppCompatActivity() {
    lateinit var memoImageDB: memoImageDb
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

        //메모
        memoImageDB = memoImageDb(baseContext,"memoImage.db",null,1)

        memoTextView.setMovementMethod(ScrollingMovementMethod())
//        memoTextView.text = memoImageDB.getMemo(photoList[pos].fileName)
//        dateTextView.text = memoImageDB.getDate(photoList[pos].fileName)
        memoTextView.text = photoList[pos].getMemo()
        dateTextView.text = photoList[pos].getDate("yyyy년 dd월 mm일")

        editMemoButton.setOnClickListener {
            var editedMemo = ""
            val ad = AlertDialog.Builder(this)

            ad.setTitle("메모 수정")
            ad.setMessage("메모를 적어주세요")

            val et = EditText(this)
            ad.setView(et)
            ad.setPositiveButton("저장") { dialog, which ->
                editedMemo = et!!.text.toString()
                Toast.makeText(this, editedMemo + " 저장되었습니다", Toast.LENGTH_SHORT).show()
                photoList[pos].imgmemo = editedMemo
                memoTextView.text = editedMemo
                dialog.dismiss()
            }
            ad.setNegativeButton("닫기") { dialog, which ->
                dialog.dismiss()
            }
            ad.show()
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