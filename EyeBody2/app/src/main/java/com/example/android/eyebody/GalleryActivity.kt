package com.example.android.eyebody

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/*
이미지 암호화
file IO stream으로 이미지 AES 암호화/복호화

갤러리 UI도 만들어야 함
 */
class GalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
    }
}
