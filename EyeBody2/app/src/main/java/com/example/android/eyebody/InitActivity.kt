package com.example.android.eyebody

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.security.MessageDigest

class InitActivity : AppCompatActivity() {

    @SuppressLint("ApplySharedPref")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)

        val goMain by lazy { Intent(this, MainActivity::class.java) }

        // TODO ----- 현재는 비밀번호를 hash로 변환하여 저장하고 있음.
        // TODO ----> 비밀번호 검증 : hash / 이미지 암호화 : AES n-bit (key = hash) / 백업 : hash
        // 유저가 비밀번호를 치면 hash값으로 변환 후 이 값을 이용하여 암호화/복호화
        // 어떻게 해야댈지 모르겠네 아.. 이건 너무 구린데.. 넘나 어렵당


        /* SharedPreferences (앱 공유 데이터)
        isUserTypeInitSetting : 유저가 처음 시작할 때 비밀번호, 몸매목표 등을 세팅했는지 확인하는 파일
        MODE_PRIVATE : 다른 앱이 접근 불가(파일 권한 없이 불가를 뜻하는 것 같음) (mode_world_readable : 다른 앱이 공유 데이터에 접근 가능)
         */
        val share: SharedPreferences = getSharedPreferences("hash-md5", Context.MODE_PRIVATE)
        val isSetPassword = share.getBoolean("isSetting", false)

        if (isSetPassword) {
            Log.d("mydbg_init", "비밀번호 초기설정이 완료되어있으므로 MainActivity로 넘어갑니다.")
            startActivity(goMain)
            finish()
        }



    }
}
