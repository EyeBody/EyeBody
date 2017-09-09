package com.example.android.eyebody

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.SurfaceHolder

/*
 * 카메라(사진촬영, 카메라가이드)
 * 카메라, 외장메모리 저장 권한
 * camera preview에서 SurfaceView 클래스 사용하면 이미지를 오버레이 할 수 있음
 */
class CameraActivity : AppCompatActivity() , SurfaceHolder.Callback {
    var stringPath = "/sdcard/sampleVideo."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

    }
    override fun surfaceCreated(p0: SurfaceHolder?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
