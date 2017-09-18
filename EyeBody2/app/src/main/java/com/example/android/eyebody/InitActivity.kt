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
import kotlinx.android.synthetic.main.activity_init.*
import java.security.MessageDigest

class InitActivity : AppCompatActivity() {

    @SuppressLint("ApplySharedPref")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)

        val goMain by lazy { Intent(this, MainActivity::class.java) }
        val tv_password = findViewById<EditText>(textview_password.id)
        val bt_passwordSubmit = findViewById<Button>(button_passwordSubmit.id)


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


        tv_password.setOnEditorActionListener { textView, i, keyEvent ->
            true
            bt_passwordSubmit.callOnClick()
        }


        bt_passwordSubmit.setOnClickListener { view ->
            val str_pw = tv_password.text
            Log.d("mydbg_init", "password >>>> $str_pw")

            if (str_pw.isNotEmpty()) {
                Toast.makeText(this, "${str_pw}를 입력하였습니다.", Toast.LENGTH_LONG).show()
                Log.d("mydbg_init", "${str_pw.length}")


                // TODO ----- MD5는 보안이 취약해 추후 바꿔야 함. SHA-3(KECCAK)을 후보로 생각하고 있음.
                // 파라미터로 charset을 넣지 않으면 값이 보존되지 않는 현상에 대해 알아봐야 함.
                val md5 = MessageDigest.getInstance("MD5")
                val pwByte = str_pw.toString().toByteArray(charset("unicode"))
                md5.update(pwByte)
                val hashedPW = md5.digest().toString(charset("unicode"))

                Log.d("mydbg_init", "평문 pw : ${pwByte.toString(charset("unicode"))}")
                Log.d("mydbg_init", "MD5 pw : $hashedPW")


                val Editor = share.edit()
                Editor.putString("hashedPW", hashedPW)
                        .putBoolean("isSetting", true)
                        .commit()
                startActivity(goMain)
                finish()

            } else {
                Toast.makeText(this, "password를 입력해주세요 (1자 이상)", Toast.LENGTH_LONG).show()
            }
        }

    }
}