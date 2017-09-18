package com.example.android.eyebody

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.hardware.Camera
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/*
 * 카메라(사진촬영, 카메라가이드)
 * 카메라, 외장메모리 저장 권한
 * camera preview에서 SurfaceView 클래스 사용하면 이미지를 오버레이 할 수 있음
 */

class CameraActivity : Activity(), SurfaceHolder.Callback {
    private var surfaceHolder: SurfaceHolder? = null
    private var camera: Camera? = null
    var now:Long=System.currentTimeMillis()
    private var previewing: Boolean = false
    var controlInflator:LayoutInflater?=null
    var IMAGE_FILE:String?=null;
    var sdf:SimpleDateFormat?= SimpleDateFormat("yyyy-MM-dd")
    var date: Date =Date(now)
    //사진 파일 이름을 날짜와 년도로 표시

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        init()

        btn_shutter.setOnClickListener{
            var shuttercallBack=Camera.ShutterCallback(){
                fun onShutter(){
                    var bitmap:Bitmap=BitmapFactory.decodeByteArray(data,0,data.length)
                }
            }
        }
    }
    private fun init(){
        window.setFormat(PixelFormat.UNKNOWN)
        surfaceHolder = cameraScreen.holder
        surfaceHolder?.addCallback(this)
        surfaceHolder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        controlInflator= LayoutInflater.from(baseContext)
        IMAGE_FILE=sdf?.format(date)
    }
    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
        if (previewing) {
            camera?.stopPreview()
            previewing = false
        }
        if (camera != null) {
            try {
                camera?.setPreviewDisplay(surfaceHolder)
                camera?.startPreview()
                previewing = true
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    override fun surfaceCreated(holder:SurfaceHolder) {
        camera = Camera.open()
    }
    override fun surfaceDestroyed(holder:SurfaceHolder) {
        camera?.stopPreview()
        camera?.release()
        camera = null
        previewing = false
    }
}
