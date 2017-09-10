package com.example.android.eyebody

import android.hardware.Camera
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.SurfaceHolder
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.IOException

/*
 * 카메라(사진촬영, 카메라가이드)
 * 카메라, 외장메모리 저장 권한
 * camera preview에서 SurfaceView 클래스 사용하면 이미지를 오버레이 할 수 있음
 */
class CameraActivity : AppCompatActivity() , SurfaceHolder.Callback {
    val stringPath = "/sdcard/sampleVideo."
    private var surfaceHolder:SurfaceHolder=cameraScreen.holder
    private var camera: android.hardware.Camera?=Camera.open()
    var previewing:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        surfaceHolder?.addCallback(this)
        surfaceHolder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

        if(camera!=null){
            try {
                camera?.setPreviewDisplay(surfaceHolder)
                camera?.startPreview()
                previewing=true;
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }
    override fun surfaceCreated(p0: SurfaceHolder?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        camera=Camera.open()
    }
    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        camera?.stopPreview()
        camera?.release()
        camera=null
        previewing=false
    }
    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
