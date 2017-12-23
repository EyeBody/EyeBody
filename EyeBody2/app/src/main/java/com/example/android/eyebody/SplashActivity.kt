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

        setContentView(R.layout.activity_splash)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(baseContext, InitActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}