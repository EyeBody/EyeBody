package com.example.android.eyebody

import android.app.Activity
import android.graphics.PixelFormat
import android.hardware.Camera
import android.os.Bundle
import android.view.SurfaceHolder
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.IOException
/*
 * 카메라(사진촬영, 카메라가이드)
 * 카메라, 외장메모리 저장 권한
 * camera preview에서 SurfaceView 클래스 사용하면 이미지를 오버레이 할 수 있음
 */

class CameraActivity : Activity(), SurfaceHolder.Callback {
    private var surfaceHolder: SurfaceHolder? = null
    private var camera: Camera? = null
    private var previewing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        window.setFormat(PixelFormat.UNKNOWN)
        surfaceHolder = cameraScreen.holder
        surfaceHolder?.addCallback(this)
        surfaceHolder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
