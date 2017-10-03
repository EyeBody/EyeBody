package com.example.android.eyebody

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_confirm.*

class ConfirmActivity : AppCompatActivity() {

    var bitmapFront:Bitmap?=null
    var bitmapSide:Bitmap?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)
        showImage()
        saveButtonClicked()
    }
    private fun showImage(){
        var intent = intent
        var frontImage = intent.extras.getByteArray("front")
        var sideImage = intent.extras.getByteArray("side")
        bitmapFront= BitmapFactory.decodeByteArray(frontImage,0,frontImage.size)
        bitmapSide= BitmapFactory.decodeByteArray(sideImage,0,sideImage.size)
        image_front.setImageBitmap(bitmapFront)
        image_side.setImageBitmap(bitmapSide)
    }
    private fun saveButtonClicked() {
        button_save.setOnClickListener {
            
        }
    }
}

