package com.example.android.eyebody

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.hardware.Camera
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.SurfaceHolder
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.IOException
/*
 * 카메라(사진촬영, 카메라가이드)
 * 카메라, 외장메모리 저장 권한
 * camera preview에서 SurfaceView 클래스 사용하면 이미지를 오버레이 할 수 있음
 */

class CameraActivity : Activity(), SurfaceHolder.Callback {
    companion object {
        val REQUEST_PERMISSION = 1
    }
    private var surfaceHolder: SurfaceHolder? = null
    private var camera: Camera? = null
    private var previewing: Boolean = false
    @RequiresApi(Build.VERSION_CODES.M)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        window.setFormat(PixelFormat.UNKNOWN)
        surfaceHolder = cameraScreen.holder
        surfaceHolder!!.addCallback(this)
        surfaceHolder!!.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }
    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_PERMISSION)
        } else {
            camera()
        }
    }
    private fun camera(){
        if (!previewing) {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)
            if (camera != null) {
                try {
                    camera!!.setPreviewDisplay(surfaceHolder)
                    camera!!.startPreview()
                    previewing = true
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION -> if (grantResults.size > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                camera()
            }
        }
    }
    override fun surfaceCreated(p0: SurfaceHolder?) {
        camera=Camera.open()
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun surfaceDestroyed(p0: SurfaceHolder?) {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
        if(previewing){
            camera.stopPreview()
            previewing=false
        }
        if(camera!=null){
            try{
                camera.setPreviewDisplay(surfaceHolder)
                camera.startPreview()
                previewing=true
            }catch (e:IOException){
                e.printStackTrace()
            }
        }
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
