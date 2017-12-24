package com.example.android.eyebody.camera

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.android.eyebody.MainActivity
import com.example.android.eyebody.R
import kotlinx.android.synthetic.main.activity_confirm.*
import java.io.File
import android.widget.EditText


class ConfirmActivity : AppCompatActivity() {
    var frontFileName: String? = null
    var sideFileName: String? = null
    var frontImageUri:String=""
    var sideImageUri:String=""
    var value:String=""
    var memoImageDB:memoImageDb?=null
    var time:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)
        memoImageDB =memoImageDb(baseContext,"memoImage.db",null,1)
        showImage()
        saveButtonClicked()
        deleteButtonClicked()
        memoButtonClicked()
    }

    //찍은 이미지를 화면에 뿌려주는 역할
    private fun showImage() {
        var intent = intent
        frontImageUri = intent.getStringExtra("frontUri")//front 이미지 uri 받아옴
        sideImageUri = intent.getStringExtra("sideUri")//side 이미지 uri
        frontFileName = intent.extras.getString("frontName")//front 이미지 파일명
        sideFileName = intent.extras.getString("sideName")//side 이미지 파일명
        time=intent.extras.getString("time")
        var sideImage = Uri.parse(sideImageUri)
        var frontImage = Uri.parse(frontImageUri)
        image_front.setImageURI(frontImage)
        image_side.setImageURI(sideImage) //이미지 두개 imageview에 맵핑함
    }

    private fun goHomeActivity() {
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }//홈으로 돌아감

    private fun goCameraActivity() {
        var intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }//카메라 엑티비티로 돌아감

    //버튼을 두개로 할 예정 삭제. 저장
    //저장버튼이 클릭되면 저장하였습니다 toast창 뜨고 홈으로 돌아감
    private fun saveButtonClicked() {
        button_save.setOnClickListener {
            Toast.makeText(applicationContext, "저장되었습니다", Toast.LENGTH_SHORT).show()
            memoImageDB!!.insert(time,frontImageUri,sideImageUri,value)
            goHomeActivity()
        }
    }

    private fun deleteFile() {
        var frontFile = File(frontFileName)
        var sideFile = File(sideFileName)
        frontFile.delete()
        sideFile.delete()
    }

    //삭제버튼 누르면 파일삭제. 다시 찍으시겠습니까?alertDialog뜨고 거절하면 홈으로 아니면 카메라 액티비티로 돌아감
    private fun deleteButtonClicked() {
        button_delete.setOnClickListener {
            val alertDilog = AlertDialog.Builder(this@ConfirmActivity).create()
            alertDilog.setTitle("삭제")
            alertDilog.setMessage("삭제 하시겠습니까?")
            alertDilog.setButton(AlertDialog.BUTTON_NEUTRAL, "삭제후 다시촬영", { dialogInterface, i ->
                deleteFile()
                Toast.makeText(applicationContext, "다시 촬영", Toast.LENGTH_SHORT).show()
                goCameraActivity()
            })

            alertDilog.setButton(AlertDialog.BUTTON_POSITIVE, "취소", { dialogInterface, j ->
            })//암것도 안함
            alertDilog.setButton(AlertDialog.BUTTON_NEGATIVE, "삭제후 홈으로", { dialogInterface, k ->
                deleteFile()
                Toast.makeText(applicationContext, "삭제되었습니다", Toast.LENGTH_SHORT).show()
                goHomeActivity()
            })
            alertDilog.show()
        }
    }
    private fun memoButtonClicked(){
        button_memo.setOnClickListener{
            val ad = AlertDialog.Builder(this@ConfirmActivity)

            ad.setTitle("메모")       // 제목 설정
            ad.setMessage("메모를 적어주세요")   // 내용 설정
// EditText 삽입하기
            val et = EditText(this@ConfirmActivity)
            ad.setView(et)
// 확인 버튼 설정
            ad.setPositiveButton("저장") { dialog, which ->
                value = et!!.text.toString()
                dialog.dismiss()     //닫기
                goHomeActivity()
            }
// 취소 버튼 설정
            ad.setNegativeButton("닫기") { dialog, which ->
                dialog.dismiss()     //닫기
                // Event
            }
// 창 띄우기
            ad.show()
        }
    }
    private fun putValuesInDb(dbHelper: memoImageDb, time:String, frontImage:String, sideImage:String,memo:String){
            memoImageDB!!.insert(time,frontImage,sideImage,memo)
    }
}

