package com.example.android.eyebody

import android.app.Activity
import android.content.Intent
import android.graphics.PixelFormat
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.hardware.Camera.ShutterCallback
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/*
 * 카메라(사진촬영, 카메라가이드)
 * 카메라, 외장메모리 저장 권한
 * camera preview에서 SurfaceView 클래스 사용하면 이미지를 오버레이 할 수 있음
 */
class CameraActivity : Activity(), SurfaceHolder.Callback {
    var TAG: String = "CameraActivity"
    var rootPath: String? = null
    private var surfaceHolder: SurfaceHolder? = null
    private var camera: Camera? = null
    private var previewing: Boolean = false
    var count: Int = 0
    private var frontImage: ByteArray? = null
    private var sideImage: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        init()
        btn_shutter.setOnClickListener {
            try {
                camera?.takePicture(shutterCallback, rawCallback, jpegCallback)
            } catch (e: RuntimeException) {
                Log.d(TAG, "take picture failed")
            }
        }
    }

    private fun changeIntent() {
        var confirmIntent = Intent(this, ConfirmActivity::class.java)
        confirmIntent.putExtra("front",frontImage)
        confirmIntent.putExtra("side",sideImage)
        startActivity(confirmIntent)

    }

    //이미 앱을 실행시킨 전적이 있으면 그냥 패스, 아니면 폴더를 생성한다.
    private fun makeFolder() {
        rootPath = getExternalFilesDir(null).toString() + "/gallery_body"
        var file = File(rootPath)
        file.mkdirs()
    }

    //TODO : 카메라 전후면 변경 토글키를 넣을까 말까고민중
    //Called as near as possible to the moment when a photo is captured from the sensor.
    private var shutterCallback = ShutterCallback {
        Log.d(TAG, "onShutter'd")
        Toast.makeText(baseContext, "shutter Clicked", Toast.LENGTH_SHORT)
    }

    private var rawCallback = PictureCallback { bytes: ByteArray?, camera: Camera? ->
        Log.d(TAG, "onPictureTaken-raw")
    }

    //TODO : 사진 저장시 용량이 터질 경우 예외처리.
    private var jpegCallback = PictureCallback { bytes: ByteArray?, camera: Camera? ->
        makeFolder()
        //TODO : 옆을 찍을때랑 앞을 찍을때랑 이름이 바뀐다.이부분은 처음찍을때랑 두번째 찍을때 플래그를 바꿔가면서 처리하는걸로 한다.
        var timeStamp: String = java.text.SimpleDateFormat("yyyyMMddHHmmss").format(Date())//파일 이름 년월날시간분초로 설정하기 위한 변수
        var fileName = String.format("body_$timeStamp.jpg")
        var path: String = rootPath + "/" + fileName

        var file = File(path)
        try {
            var fos = FileOutputStream(file)
            fos.write(bytes)
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        var intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        var uri = Uri.parse("file://" + path)
        intent.data = uri
        sendBroadcast(intent)
        count++
        if (count == 2) {
            sideImage = bytes
            count = 0
            changeIntent()
        } else {
            frontImage = bytes
            camera?.startPreview()
        }
    }

    private fun init() {
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

