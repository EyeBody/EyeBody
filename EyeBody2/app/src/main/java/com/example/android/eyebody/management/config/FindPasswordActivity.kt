package com.example.android.eyebody.management.config

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.android.eyebody.EncryptStringManager
import com.example.android.eyebody.R
import kotlinx.android.synthetic.main.activity_find_password.*

/**
 * Created by Yoon on 2017-11-24
 */
class FindPasswordActivity: AppCompatActivity(){
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
        emailButton.isClickable = pref_email_auth == getString(R.string.sharedPreference_email_auth_ok)
        inputKeyButton.isClickable = emailButton.isClickable

        if(emailButton.isClickable){
            emailButton.setOnClickListener {

                //TODO random text generater 를 만들거나 5회이상 입력시 5분 뒤 시도하게 만들기
                var key : Int? = (Math.random()*1_000_000).toInt()
                /* TODO 이메일로 key 를 보낸다.
                관련 api는 아래와 같음.
                 * https://stackoverflow.com/questions/2020088/sending-email-in-android-using-javamail-api-without-using-the-default-built-in-a
                  * */

                val encryptKey = EncryptStringManager.encryptString(key.toString())

                pref.edit()
                        .putString(getString(R.string.email_auth_key), encryptKey)
                        .commit()
            }
            inputKeyButton.setOnClickListener {
                val encryptInput = EncryptStringManager.encryptString(inputKey.text.toString())
                if(encryptInput == pref.getString(getString(R.string.email_auth_key), "None")){
                    Toast.makeText(this, "맞았음. 이제 비밀번호 변경으로 가면 됨", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}