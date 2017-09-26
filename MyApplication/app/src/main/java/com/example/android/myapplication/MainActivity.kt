package com.example.android.myapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    public void onClick(View arg0) {
        // TODO Auto-generated method stub camera.takePicture(myShutterCallback, myPictureCallback_RAW, myPictureCallback_JPG); }}); } ShutterCallback myShutterCallback = new ShutterCallback(){ @Override public void onShutter() { // TODO Auto-generated method stub }}; PictureCallback myPictureCallback_RAW = new PictureCallback(){ @Override public void onPictureTaken(byte[] arg0, Camera arg1) { // TODO Auto-generated method stub }}; PictureCallback myPictureCallback_JPG = new PictureCallback(){ @Override public void onPictureTaken(byte[] arg0, Camera arg1) { // TODO Auto-generated method stub Bitmap bitmapPicture = BitmapFactory.decodeByteArray(arg0, 0, arg0.length); }};
    }

}
