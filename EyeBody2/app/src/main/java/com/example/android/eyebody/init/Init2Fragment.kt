package com.example.android.eyebody.init

import android.annotation.SuppressLint
import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.android.eyebody.utility.EncryptStringManager
import com.example.android.eyebody.R

/**
 * Created by YOON on 2017-09-24
 */

// password 설정
// progress dot (2/3)

@SuppressLint("ApplySharedPref")
class Init2Fragment : Fragment() {

    var imm: InputMethodManager? = null
    var viewPassword:EditText?=null
    // 비밀번호를 hash로 변환(md5, unicode)하여 저장
    // TODO ----> 비밀번호 검증 : hash / 이미지 암호화 : AES n-bit (key = hash) / 백업 : hash
    // 유저가 비밀번호를 치면 hash값으로 변환 후 이 값을 이용하여 암호화/복호화

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        val v = inflater!!.inflate(R.layout.fragment_init2, container, false)
        Log.d("mydbg_init2", "[ init2 진입 ]")

        viewPassword = v.findViewById<EditText>(R.id.EditText_input_password)
        val viewPasswordSubmit = v.findViewById<Button>(R.id.Button_submit_password)
        imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager


        viewPassword!!.setOnEditorActionListener { textView, i, keyEvent ->
            viewPasswordSubmit.callOnClick()
        }


        viewPasswordSubmit.setOnClickListener { view ->
            hideKeyboard()
            val strPW = viewPassword!!.text
            Log.d("mydbg_init2", "password >>> $strPW")

            if (strPW.isNotEmpty()) {
                Toast.makeText(activity, "${strPW}를 입력하였습니다.", Toast.LENGTH_LONG).show()
                Log.d("mydbg_init2", "  length >>> ${strPW.length}")


                // TODO ----- MD5 에서 SHA-3 (KECCAK) or SHA128 로 알고리즘 개선
                // MIT license code : https://github.com/walleth/keccak/blob/master/keccak/src/main/kotlin/org/walleth/keccak/Keccak.kt
                val pwByte = strPW.toString().toByteArray(charset("unicode"))
                val hashedPW = EncryptStringManager.encryptString(strPW.toString())

                Log.d("mydbg_init2", " 평문 pw >>> ${pwByte.toString(charset("unicode"))}")
                Log.d("mydbg_init2", "  MD5 pw >>> $hashedPW")

                activity.getSharedPreferences(getString(R.string.getSharedPreference_initSetting), Context.MODE_PRIVATE)
                        .edit()
                        .putString("hashedPW", hashedPW)
                        .commit()

                val init3 = Init3Fragment()
                activity.fragmentManager.beginTransaction()
                        .replace(R.id.fragment_init_content, init3)
                        .commit()

                val initActivity : InitActivity = activity as InitActivity
                initActivity.setButtonVisibility(View.VISIBLE, View.INVISIBLE)
            } else {
                Toast.makeText(activity, "password를 입력해주세요 (1자 이상)", Toast.LENGTH_LONG).show()
            }
        }
        return v
    }
    private fun hideKeyboard()
    {
        imm!!.hideSoftInputFromWindow(viewPassword!!.windowToken, 0);
    }
}