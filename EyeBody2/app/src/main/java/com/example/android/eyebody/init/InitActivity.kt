package com.example.android.eyebody.init

import android.annotation.SuppressLint
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.android.eyebody.MainActivity
import com.example.android.eyebody.R
import java.security.MessageDigest


// 상단에 init N flagment 를 띄워준다.
// progress dot (flagment에 따라 설정)
// TODO 취소버튼 2번누르면 finish() 하게 하기

class InitActivity : AppCompatActivity() {

    @SuppressLint("ApplySharedPref")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)

        /* SharedPreferences (앱 공유 데이터)
        isUserTypeInitSetting : 유저가 처음 시작할 때 비밀번호, 몸매목표 등을 세팅했는지 확인하는 파일
        MODE_PRIVATE : 다른 앱이 접근 불가(파일 권한 없이 불가를 뜻하는 것 같음) (mode_world_readable : 다른 앱이 공유 데이터에 접근 가능)
         */
        val sharedPref: SharedPreferences = getSharedPreferences("hash-md5", Context.MODE_PRIVATE)
        val isSetPassword = sharedPref.getBoolean("isSetting", false)
        val isSetTarget = sharedPref.getBoolean("isSetTarget",false)


        //debug start (password injection)-------------------------------------------------
        // TODO (fragment) frag 추가 하면 이 부분 없어도 됨.
        val md5 = MessageDigest.getInstance("MD5")
        val pwByte = "password".toByteArray(charset("unicode"))
        md5.update(pwByte)
        val hashedPW = md5.digest().toString(charset("unicode"))

        val spEditor = sharedPref.edit()
        spEditor.putString("hashedPW", hashedPW)
                .putBoolean("isSetting", true)
                .commit()
        //debug end----------------------------------------------------------------------


        if (isSetPassword && isSetTarget) {
            Log.d("mydbg_init", "초기설정(비밀번호-init2, 목표-init3)이 완료되어있으므로 MainActivity로 넘어갑니다.")

            val goMain = Intent(this, MainActivity::class.java)
            startActivity(goMain)
            finish()
        }


    }
}