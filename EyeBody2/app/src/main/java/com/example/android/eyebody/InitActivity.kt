package com.example.android.eyebody

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Debug
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_init.*
import kotlinx.android.synthetic.main.activity_main.*

class InitActivity : AppCompatActivity() {

    @SuppressLint("ApplySharedPref")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)

        val goMain by lazy{ Intent(this,MainActivity::class.java) }

        /* SharedPreferences (앱 공유 데이터)
        isUserTypeInitSetting : 유저가 처음 시작할 때 비밀번호, 몸매목표 등을 세팅했는지 확인하는 파일
        MODE_PRIVATE : 다른 앱이 접근 불가(파일 권한 없이 불가를 뜻하는 것 같음) (mode_world_readable : 다른 앱이 공유 데이터에 접근 가능)
         */
        // TODO("공유데이터로 initActivity를 실행하게? 아니면 이닛에서 공유데이터를 판별할지 해야함")
        // TODO("현재는 비밀번호를 그대로 저장하지만 암호화기법을 사용하여 앱을 뜯었을 때도 알 수 없게 해야 함."
        val share : SharedPreferences = getSharedPreferences("pref1", Context.MODE_PRIVATE)
        val isSetPassword = share.getBoolean("isSetting",false)
        if(isSetPassword) {
            Log.d("initt","비밀번호 초기설정이 완료되어있으므로 MainActivity로 넘어갑니다.")
            startActivity(goMain)
            finish()
        }

        val tv_password = findViewById<EditText>(textview_password.id)
        val bt_passwordSubmit = findViewById<Button>(button_passwordSubmit.id)

        val str_password = tv_password.text

        bt_passwordSubmit.setOnClickListener {

            Log.d("initt","password >>>> ${tv_password.text.toString()}")

            if(tv_password.text != null){
                val Editor = share.edit()
                Editor.putString("password",str_password.toString())
                        .putBoolean("isSetting",true)
                        .commit()
                startActivity(goMain)
                finish()
                // TODO("왜 안죽어")
            } else {
                Toast.makeText(this,"password를 입력해주세요 (1자 이상)", Toast.LENGTH_LONG).show()
            }
        }


    }
}