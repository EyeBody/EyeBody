package com.example.android.eyebody.init

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.android.eyebody.utility.StringHashManager
import com.example.android.eyebody.R
import com.example.android.eyebody.management.ManagementActivity
import kotlinx.android.synthetic.main.fragment_init2.*

/**
 * Created by YOON on 2017-09-24
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
        val v = inflater!!.inflate(R.layout.fragment_init2, container, false)
        Log.d("mydbg_init2", "[ init2 진입 ]")

        val viewPassword = v.findViewById<EditText>(R.id.editText_input_password)
        val viewPasswordConfirm = v.findViewById<EditText>(R.id.editText_input_password_confirm)
        val viewPasswordSubmit = v.findViewById<Button>(R.id.button_submit_password)

        viewPassword.setOnEditorActionListener { textView, i, keyEvent ->
            viewPasswordConfirm.requestFocus()
        }
        viewPasswordConfirm.setOnEditorActionListener { textView, i, keyEvent ->
            viewPasswordSubmit.callOnClick()
            val imm: InputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
            true
        }


        viewPasswordSubmit.setOnClickListener { view ->
            val strPW = viewPassword.text
            val strConfirmPW = editText_input_password_confirm.text
            Log.d("mydbg_init2", "password >>> $strPW / $strConfirmPW")

            if (strPW.isNotEmpty() && strPW.toString() == strConfirmPW.toString()) {
                //Toast.makeText(activity, "${strPW}를 입력하였습니다.", Toast.LENGTH_LONG).show()
                Log.d("mydbg_init2", "  length >>> ${strPW.length}")


                // TODO ----- MD5 에서 SHA-3 (KECCAK) or SHA128 로 알고리즘 개선
                // MIT license code : https://github.com/walleth/keccak/blob/master/keccak/src/main/kotlin/org/walleth/keccak/Keccak.kt
                val hashedPW = StringHashManager.encryptString(strPW.toString())
                val pwByte = strPW.toString().toByteArray(charset("unicode"))
                Log.d("mydbg_init2", " 평문 pw >>> ${pwByte.toString(charset("unicode"))}")
                Log.d("mydbg_init2", "  MD5 pw >>> $hashedPW")

                activity.getSharedPreferences(getString(R.string.getSharedPreference_initSetting), Context.MODE_PRIVATE)
                        .edit()
                        .putString("hashedPW", hashedPW)
                        .commit()

                //메인으로 이동
                var intent = Intent(activity, ManagementActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("isFirst", true)
                startActivity(intent)
                activity.finish()
            } else {
                Toast.makeText(activity, "password를 입력해주세요 (1자 이상)", Toast.LENGTH_LONG).show()
            }
        }


        return v
    }


}