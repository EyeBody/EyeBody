package com.example.android.eyebody

import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import com.example.android.eyebody.init.InitActivity
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Created by ytw11 on 2017-11-27.
 * splash activity
 */

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.activity_splash)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //var typeFace = Typeface.createFromAsset(assets, "fonts/arita.ttf")  //asset > fonts 폴더 내 폰트파일 적용
        //text.typeface = typeFace  //안드로이드 폰트 지정

        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(baseContext, InitActivity::class.java)
            startActivity(intent)
            finish()
        }, 500)
    }
}

