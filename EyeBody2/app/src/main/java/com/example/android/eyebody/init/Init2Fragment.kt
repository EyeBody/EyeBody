package com.example.android.eyebody.init

import android.annotation.SuppressLint
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.android.eyebody.MainActivity
import com.example.android.eyebody.R
import java.security.MessageDigest

/**
 * Created by YOON on 2017-09-24.
 */

// password 설정
// progress dot (2/3)

@SuppressLint("ApplySharedPref")
class Init2Fragment : Fragment() {


    // 비밀번호를 hash로 변환(md5, unicode)하여 저장
    // TODO ----> 비밀번호 검증 : hash / 이미지 암호화 : AES n-bit (key = hash) / 백업 : hash
    // 유저가 비밀번호를 치면 hash값으로 변환 후 이 값을 이용하여 암호화/복호화

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        Log.d("mydbg_init2","init2 진입")

        val viewPassword = view.findViewById<EditText>(R.id.textview_password)
        val viewPasswordSubmit = view.findViewById<Button>(R.id.button_passwordSubmit)

        viewPassword.setOnEditorActionListener { textView, i, keyEvent ->
            viewPasswordSubmit.callOnClick()
        }


        viewPasswordSubmit.setOnClickListener { view ->
            val strPW = viewPassword.text
            Log.d("mydbg_init", "password >>>> $strPW")

            if (strPW.isNotEmpty()) {
                Toast.makeText(activity, "${strPW}를 입력하였습니다.", Toast.LENGTH_LONG).show()
                Log.d("mydbg_init", "${strPW.length}")


                // TODO ----- MD5 에서 SHA-3 (KECCAK) or SHA128 로 알고리즘 개선
                // MIT license code : https://github.com/walleth/keccak/blob/master/keccak/src/main/kotlin/org/walleth/keccak/Keccak.kt
                val md5 = MessageDigest.getInstance("MD5")
                val pwByte = strPW.toString().toByteArray(charset("unicode"))
                md5.update(pwByte)
                val hashedPW = md5.digest().toString(charset("unicode"))

                Log.d("mydbg_init", "평문 pw : ${pwByte.toString(charset("unicode"))}")
                Log.d("mydbg_init", "MD5 pw : $hashedPW")


                val sharedPref: SharedPreferences = activity.getSharedPreferences("hash-md5", Context.MODE_PRIVATE)
                val spEditor = sharedPref.edit()
                spEditor.putString("hashedPW", hashedPW)
                        .putBoolean("isSetting", true)
                        .commit()
                val goMain = Intent(activity, MainActivity::class.java)
                startActivity(goMain)
                activity.finish()
            } else {
                Toast.makeText(activity, "password를 입력해주세요 (1자 이상)", Toast.LENGTH_LONG).show()
            }
        }


        return super.onCreateView(inflater, container, savedInstanceState)
    }


}