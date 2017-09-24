package com.example.android.eyebody

import android.app.Activity
import android.content.ContentValues
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.SurfaceHolder
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.FileNotFoundException
import java.io.IOException
import java.io.OutputStream

/*
 * 카메라(사진촬영, 카메라가이드)
 * 카메라, 외장메모리 저장 권한
 * camera preview에서 SurfaceView 클래스 사용하면 이미지를 오버레이 할 수 있음
 */
class CameraActivity : Activity(), SurfaceHolder.Callback {
    var TAG: String = "CameraActivity"
    private var surfaceHolder: SurfaceHolder? = null
    private var camera: Camera? = null
    private var previewing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        init()
        btn_shutter.setOnClickListener {
            try {
                camera?.takePicture(shutterCallback, rawCallback, jpegCallback)
            }catch(e:RuntimeException){
                Log.d(TAG,"take picture failed")
            }
        }
    }
    //Called as near as possible to the moment when a photo is captured from the sensor.
    private var shutterCallback: Camera.ShutterCallback = Camera.ShutterCallback {
        fun onShutter() {
            Log.d(TAG, "onShutter'd")
        }
    }
    private var rawCallback = Camera.PictureCallback { bytes: ByteArray?, camera: Camera? ->
        fun onPictureTaken(bytes: ByteArray, camera: Camera) {
            Log.d(TAG, "onPictureTaken-raw")
        }
    }
    private var jpegCallback = PictureCallback { bytes: ByteArray?, camera: Camera? ->
        fun onPictureTaken(bytes: ByteArray, camera: Camera) {
            var bitmapPicture: Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            var uriTarget: Uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())
            Log.d(TAG, "onPictureTaken - jp")
            var imageFile: OutputStream? = null
            try {
                imageFile = contentResolver.openOutputStream(uriTarget)
                imageFile.write(bytes)
                imageFile.flush()
                imageFile.close()
                Toast.makeText(baseContext,"image save",Toast.LENGTH_SHORT)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            camera?.startPreview()
        }
        camera?.startPreview()
    }

    //셔터 버튼이 눌리면 실행.
    //setCameraDisplayOrientation 함수 -> 카메라가 회전하는대로 카메라도 회전하도록 설정
   /* fun setCameraDisplayOrientation(activity: Activity, cameraId: Int, camera: Camera): Int {
        var info = android.hardware.Camera.CameraInfo()
        android.hardware.Camera.getCameraInfo(cameraId, info)
        var rotation = activity.windowManager.defaultDisplay.rotation
        var degree: Int = 0
        when (rotation) {
            Surface.ROTATION_0 -> degree = 0
            Surface.ROTATION_90 -> degree = 90
            Surface.ROTATION_180 -> degree = 180
            Surface.ROTATION_270 -> degree = 270
        }
        var result: Int=0
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degree) % 360
            result = (360 - result) % 360
        } else {
            result = (info.orientation - degree + 360) % 360
        }
        return result
    }
    */
    private fun init() {
        window.setFormat(PixelFormat.UNKNOWN)
        surfaceHolder = cameraScreen.holder
        surfaceHolder?.addCallback(this)
        requestedOrientation= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        surfaceHolder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        //controlInflator = LayoutInflater.from(baseContext)
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

    override fun surfaceCreated(holder: SurfaceHolder) {
        camera = Camera.open()
    }
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        camera?.stopPreview()
        camera?.release()
        camera = null
        previewing = false
    }
}