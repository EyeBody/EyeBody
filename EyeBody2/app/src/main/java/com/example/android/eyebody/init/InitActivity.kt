package com.example.android.eyebody.init

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import android.widget.ImageButton
import com.example.android.eyebody.MainActivity
import com.example.android.eyebody.R
import com.example.android.eyebody.TypefaceSpan

// 상단에 init N flagment 를 띄워준다.
// progress dot (flagment에 따라 설정)
// TODO 취소버튼 2번누르면 finish() 하게 하기

class InitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)

        //앱 바 커스텀 폰트 적용
        var s: SpannableString = SpannableString(getString(R.string.app_name));
        s.setSpan(TypefaceSpan(this, "roboto_regular.ttf"), 0, s.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        supportActionBar!!.setTitle(s)  //v7은 actionBar 대신 supportActionBar로 호출해야 함

        /* SharedPreferences (앱 공유 데이터)
        MODE_PRIVATE : 다른 앱이 접근 불가(파일 권한 없이 불가를 뜻하는 것 같음) (mode_world_readable : 다른 앱이 공유 데이터에 접근 가능)
         */
        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.getSharedPreference_initSetting), Context.MODE_PRIVATE)
        val sharedPref_hashedPW = sharedPref.getString(
                getString(R.string.sharedPreference_hashedPW), getString(R.string.sharedPreference_default_hashedPW))
        val sharedPref_targetWeight = sharedPref.getInt(
                getString(R.string.sharedPreference_targetWeight), resources.getInteger(R.integer.sharedPreference_default_targetWeight))
        val sharedPref_targetDate = sharedPref.getString(
                getString(R.string.sharedPreference_targetDate), getString(R.string.sharedPreference_default_targetDate))


        if (sharedPref_hashedPW != getString(R.string.sharedPreference_default_hashedPW) &&
                sharedPref_targetDate != getString(R.string.sharedPreference_default_targetDate) &&
                sharedPref_targetWeight != resources.getInteger(R.integer.sharedPreference_default_targetWeight)
                ) {
            Log.d("mydbg_init", "초기설정(비밀번호-init2, 목표-init3)이 완료되어있으므로 MainActivity로 넘어갑니다.")

            val goMain = Intent(this, MainActivity::class.java)
            startActivity(goMain)
            finish()
        } else {
            Log.d("mydbg_init", "현재 설정값은\nhashedPW = $sharedPref_hashedPW\ntargetWeight = $sharedPref_targetWeight\ntargetDate = $sharedPref_targetDate\n")
        }

    }

    /** view가 만들어진 후 호출되어야 함.
     */
    fun setButtonVisibility(leftVisibility : Int, rightVisibility : Int){
        val leftButton = findViewById<ImageButton>(R.id.button_init_prevButton)
        val rightButton = findViewById<ImageButton>(R.id.button_init_nextButton)

        leftButton.visibility = leftVisibility
        rightButton.visibility = rightVisibility
    }
}