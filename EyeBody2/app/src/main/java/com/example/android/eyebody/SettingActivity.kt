package com.example.android.eyebody

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.android.eyebody.management.ManagementActivity
import kotlinx.android.synthetic.main.activity_setting.*
import java.security.MessageDigest

// 임시 UI 만드는 곳
// 비번입력하고 SchedulerActivity로 넘어감.
class SettingActivity : AppCompatActivity() {

    val TAG = "mydbg_setting"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        Log.d(TAG, "setting 진입")

        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.getSharedPreference_initSetting), Context.MODE_PRIVATE)
        val sharedPref_hashedPW = sharedPref.getString(
                getString(R.string.sharedPreference_hashedPW), getString(R.string.sharedPreference_default_hashedPW))


        // 키보드-확인 눌렀을 때 반응
        setting_input_pw.setOnEditorActionListener { textView, i, keyEvent ->
            Log.d(TAG, "키보드-확인 누름")

            // EditText 친 부분 MD5 암호화
            val md5 = MessageDigest.getInstance("MD5")
            val str_inputPW: String = setting_input_pw.text.toString()
            val pwByte: ByteArray = str_inputPW.toByteArray(charset("unicode"))
            md5.update(pwByte)
            val hashedPW = md5.digest().toString(charset("unicode"))

            // 공유변수와 일치여부 검증
            Log.d(TAG, "prePW : $sharedPref_hashedPW / PW : $hashedPW")
            if (hashedPW == sharedPref_hashedPW.toString()) {
                val schedulerPage = Intent(this, ManagementActivity::class.java)
                startActivity(schedulerPage)
                finish()
            } else {
                Toast.makeText(this, "관계자 외 출입금지~", Toast.LENGTH_LONG).show()
            }

            true
        }

    }
}
