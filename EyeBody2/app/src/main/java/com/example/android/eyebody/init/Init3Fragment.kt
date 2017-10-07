package com.example.android.eyebody.init

import android.annotation.SuppressLint
import android.os.Bundle
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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

// 시작하기 버튼에 잔잔하게 움직이는 배경 or something
// progress dot (3/3)

class Init3Fragment : Fragment() {

    @SuppressLint("ApplySharedPref")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        val v = inflater!!.inflate(R.layout.fragment_init3, container, false)

        Log.d("mydbg_init3","init3 진입")



        val viewPassword = v.findViewById<EditText>(R.id.EditText_input_password)
        val viewPasswordSubmit = v.findViewById<Button>(R.id.Button_submit_password)

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

                val init3 = Init3Fragment()
                activity.fragmentManager.beginTransaction()
                        .replace(R.id.fragment_init_content, init3)
                        .commit()

            } else {
                Toast.makeText(activity, "password를 입력해주세요 (1자 이상)", Toast.LENGTH_LONG).show()
            }
        }




        val goMain = Intent(activity, MainActivity::class.java)
        startActivity(goMain)
        activity.finish()

        return v
    }

}