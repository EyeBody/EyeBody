package com.example.android.eyebody.camera

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
import com.example.android.eyebody.R
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
    private var rootPath: String? = null
    private var surfaceHolder: SurfaceHolder? = null
    private var camera: Camera? = null
    private var previewing: Boolean = false
    private var count: Int = 0
    private var frontImage: ByteArray? = null
    private var sideImage: ByteArray? = null
    private var frontImageName:String?=null
    private var sideImageName:String?=null
   // private var controlInflater:LayoutInflater=LayoutInflater.from(baseContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        init()
        shutterButtonClicked()
        //setLayout()
    }//TODO : 화면위에 씌우는거 해야함
    /*private fun setLayout()
    {
        var viewControl=controlInflater.inflate(R.layout.guide_pic,null)
        var layoutParamsControl= ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT)
        this.addContentView(viewControl,layoutParamsControl)
    }*/
    private fun setTextView() {
        textView_setOrder.text="옆면을 찍어주세요"
    }//앞을 찍을지 옆을 찍을지 말해주는 기능
    private fun changeImage(){

    }
    private fun shutterButtonClicked(){
        btn_shutter.setOnClickListener {
            try {
                camera?.takePicture(shutterCallback, rawCallback, jpegCallback)
            } catch (e: RuntimeException) {
                Log.d(TAG, "take picture failed")
            }
        }
    }
    private fun goConfirmActivity() {
        var confirmIntent = Intent(this, ConfirmActivity::class.java)
        confirmIntent.putExtra("front",frontImage)
        confirmIntent.putExtra("side",sideImage)
        confirmIntent.putExtra("frontName",frontImageName)
        confirmIntent.putExtra("sideName",sideImageName)
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
        setTextView()
        var timeStamp: String = java.text.SimpleDateFormat("yyyyMMddHHmmss").format(Date())//파일 이름 년월날시간분초로 설정하기 위한 변수

        var fileName = String.format("body_$timeStamp.eyebody")
        if(count==0){
            frontImageName=rootPath+"/"+fileName
        }else{
            changeImage()
            sideImageName=rootPath+"/"+fileName
        }
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
            goConfirmActivity()
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