package com.example.android.eyebody.camera

import android.app.Activity
import android.content.Intent
import android.graphics.PixelFormat
import android.hardware.Camera
import android.hardware.Camera.PictureCallback
import android.hardware.Camera.ShutterCallback
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.util.Log
import android.view.*
import android.view.ViewGroup.LayoutParams
import android.widget.Toast
import com.example.android.eyebody.MainActivity
import com.example.android.eyebody.R
import com.example.android.eyebody.management.ManagementActivity
import com.example.android.eyebody.management.main.MainManagementFragment
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
    private var frontImageUri: Uri? = null
    private var sideImageUri: Uri? = null
    private var frontImage: ByteArray? = null
    private var sideImage: ByteArray? = null
    private var frontImageName: String? = null
    private var sideImageName: String? = null
    private var controlInflater: LayoutInflater? = null
    private var viewControl: View ?=null
    var timeStamp:String=""
    var gestureObject:GestureDetectorCompat?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        init()
        shutterButtonClicked()
        setLayout()
        gestureObject = GestureDetectorCompat(this, LearnGesture())
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        this.gestureObject!!.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    private fun init() {
        window.setFormat(PixelFormat.UNKNOWN)
        surfaceHolder = cameraScreen.holder
        surfaceHolder?.addCallback(this)
        surfaceHolder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }//초기화함수

    private fun setLayout() {
        controlInflater = LayoutInflater.from(baseContext)
        viewControl = controlInflater?.inflate(R.layout.front_guide_pic, null)
        var layoutParamsControl = LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT)
        this.addContentView(viewControl, layoutParamsControl)
    }
    //이미지 가이드 표시 함수

    private fun setTextView() {
        textView_setOrder.text = "옆면을 찍어주세요"
    }//앞을 찍을지 옆을 찍을지 말해주는 함수

    private fun changeImage() {
        (viewControl?.parent as ViewManager).removeView(viewControl)
        controlInflater = LayoutInflater.from(baseContext)
        viewControl = controlInflater?.inflate(R.layout.side_guide_pic, null)
        var layoutParamsControl = LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT)
        this.addContentView(viewControl, layoutParamsControl)
    }//이미지 가이드 변경 함수
    //TODO : 이미지 위에 올리는 가이드를 전에 찍은 사진으로 한다.

    private fun shutterButtonClicked() {
        btn_shutter.setOnClickListener {
            try {
                camera?.takePicture(null, null, jpegCallback)
            } catch (e: RuntimeException) {
                Log.d(TAG, "take picture failed")
            }
        }
    }//셔터 버튼 눌리면 실행되는 함수

    private fun goConfirmActivity() {
        var confirmIntent = Intent(this, ConfirmActivity::class.java)
        confirmIntent.putExtra("frontUri", frontImageUri.toString())
        confirmIntent.putExtra("sideUri", sideImageUri.toString())
        confirmIntent.putExtra("frontName", frontImageName)
        confirmIntent.putExtra("sideName", sideImageName)
        confirmIntent.putExtra("time",timeStamp)
        startActivity(confirmIntent)
    }//확인창으로 넘어가는 함수

    //이미 앱을 실행시킨 전적이 있으면 그냥 패스, 아니면 폴더를 생성한다.
    private fun makeFolder() {
        rootPath = getExternalFilesDir(null).toString() + "/gallery_body"
        var file = File(rootPath)
        file.mkdirs()
    }

    //TODO : 사진 저장시 용량이 터질 경우 예외처리.
    private var jpegCallback = PictureCallback { bytes: ByteArray?, camera: Camera? ->
        makeFolder()
        Toast.makeText(baseContext, "make file success", Toast.LENGTH_SHORT)
        showPreview()//이미지 프리뷰실행
        changeImage()//가이드 이미지 변경
        //TODO : 여기에 preview 들어가야함
        setTextView()//위 문구 변경
        timeStamp= java.text.SimpleDateFormat("yyyyMMddHHmmss").format(Date())//파일 이름 년월날시간분초로 설정하기 위한 변수

        var fileName: String? = null//파일 이름 설정

        if (count == 0) {
            fileName = String.format("front_$timeStamp.jpg")
            frontImageName = rootPath + "/" + fileName//front 이미지 파일 경로 + 이름
            frontImageUri = Uri.fromFile(File(rootPath, fileName))
        } else {
            fileName = String.format("side_$timeStamp.jpg")
            sideImageName = rootPath + "/" + fileName//side 이미지 파일 경로 + 이름
            sideImageUri = Uri.fromFile(File(rootPath, fileName))
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
/*
        var intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        var uri = Uri.parse("file://" + path)
        intent.data = uri
        sendBroadcast(intent)*/
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

    private fun showPreview(){
        image_preview.setImageURI(frontImageUri)
    }
    //TODO : 전에 찍은 이미지가 올라와야 한다.
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
        camera!!.setDisplayOrientation(90)
        try{
            var width:Int?=0
            var height:Int?=0
            var rotation=90
            val parameters=camera?.parameters

            width=640
            height=480

            parameters?.setPictureSize(width,height)
            parameters?.setRotation(rotation)
            camera?.parameters=parameters
        }catch (e:IOException){
            camera?.release()
            camera=null
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        camera?.stopPreview()
        camera?.release()
        camera = null
        previewing = false
    }
    private fun gobackHomeActivity()
    {
        var homeIntent = Intent(this, ManagementActivity::class.java)
        startActivity(homeIntent)
    }
    override fun onDestroy() {

        super.onDestroy()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        gobackHomeActivity()
    }

    internal inner class LearnGesture : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(event1: MotionEvent, event2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            if (event2.x < event1.x || event1.x == event2.x) {
                val intent = Intent(this@CameraActivity, ManagementActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
                finish()
            }
            return true
        }
    }//어떻게 밀어서 다른 액티비티로 갈것인지 결정
}