package com.example.android.eyebody.camera

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.android.eyebody.R
import kotlinx.android.synthetic.main.activity_confirm.*
import kotlinx.android.synthetic.main.activity_sticker.*

class StickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sticker)
    }
    private fun saveButtonClicked(){
        button_imageSave.setOnClickListener {

        }
    }//TODO : 저장버튼 눌리면 인텐트 받아서 저장.

}
