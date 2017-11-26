package com.example.android.eyebody.management.config

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.android.eyebody.utility.EncryptStringManager
import com.example.android.eyebody.R
import com.example.android.eyebody.utility.SendGmailManager
import kotlinx.android.synthetic.main.activity_find_password.*

/**
 * Created by Yoon on 2017-11-24
 */
class FindPasswordActivity : AppCompatActivity() {

    val TAG = "mydbg_FindPwActivity"

    @SuppressLint("ApplySharedPref")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_password)

        val email = pre_registered_mail
        val emailButton = send_temporary_key
        val inputKey = input_temporary_key
        val inputKeyButton = validation_temporary_key

        val pref = applicationContext.getSharedPreferences(getString(R.string.getSharedPreference_initSetting), Context.MODE_PRIVATE)
        val pref_email = pref.getString(getString(R.string.sharedPreference_email), "None")
        val pref_email_auth = pref.getString(getString(R.string.sharedPreference_email_auth_status), "None")

        email.text = pref_email
        emailButton.isClickable = true//pref_email_auth == getString(R.string.sharedPreference_email_auth_ok)
        inputKeyButton.isClickable = emailButton.isClickable

        if (emailButton.isClickable) {
            emailButton.setOnClickListener {

                //TODO random text generater 를 만들거나 5회이상 입력시 5분 뒤 시도하게 만들기
                val key: Int? = (Math.random() * 1_000_000).toInt()

                val mailSender = SendGmailManager.Builder()
                        .addSenderId("temporary.mail.for.programming@gmail.com")
                        .addSenderPw("temporary.mail")
                        .addSenderVisibleId("admin@eyebody.noreply.com") // 구글에서 지원을 안한다 ㅠㅠ : https://stackoverflow.com/a/4189363/7354469
                        .addReceiverId(pref_email)
                        //.addCcReceiver("myenggeun44@naver.com") //참조
                        //.addBccReceiver("tiger940404@naver.com") //숨은 참조
                        .addSubject("<Eyebody> :: 비밀번호 수정을 위한 임시 키를 발급해드렸습니다.")
                        .addBody("<a href='www.google.com'>비밀번호</a> 수정을 위한 임시 키는 $key 입니다.\n감사합니다.")
                        .addBodyType("text/html")
                        .build()
                Log.d(TAG, "보내기 시도")

                object: Thread(){
                    override fun run() {
                        mailSender?.send()
                    }
                }.start()

                val encryptKey = EncryptStringManager.encryptString(key.toString())

                pref.edit()
                        .putString(getString(R.string.email_auth_key), encryptKey)
                        .commit()
            }
            inputKeyButton.setOnClickListener {
                val encryptInput = EncryptStringManager.encryptString(inputKey.text.toString())
                if (encryptInput == pref.getString(getString(R.string.email_auth_key), "None")) {
                    Toast.makeText(this, "맞았음. 이제 비밀번호 변경으로 가면 됨", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}