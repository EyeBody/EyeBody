package com.example.android.eyebody.gallery

import android.content.Intent
import android.content.res.AssetManager
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.android.eyebody.R
import android.widget.Toast
import com.example.android.eyebody.googleDrive.GoogleDriveManager
import kotlinx.android.synthetic.main.activity_gallery.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class GalleryActivity : AppCompatActivity() {
    var photoList = ArrayList<Photo>()
    var googleDriveManager: GoogleDriveManager? = null
    var togleItem : MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        googleDriveManager = object : GoogleDriveManager(baseContext, this@GalleryActivity) {

            override fun onConnectionStatusChanged() {
                super.onConnectionStatusChanged()
                if(togleItem != null) {
                    if (googleDriveManager?.checkConnection() == true) {
                        togleItem?.title = getString(R.string.googleDrive_do_signOut)
                        Toast.makeText(activity, "connect",Toast.LENGTH_LONG).show()
                    } else {
                        togleItem?.title = getString(R.string.googleDrive_do_signIn)
                        Toast.makeText(activity, "connect xxx", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }

        //(이미지 리사이징)뷰가 그려지기 전이라서 width, height를 측정해서 가져옴
        selectedImage_gallery.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        var measuredWidth = selectedImage_gallery.measuredWidth
        var measuredHeight = selectedImage_gallery.measuredHeight

        //이미지 불러오기
        var state: String = Environment.getExternalStorageState()   //외부저장소(SD카드)가 마운트되었는지 확인
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            //디렉토리 생성
            var filedir: String = getExternalFilesDir(null).toString() + "/gallery_body"  //Android/data/com.example.android.eyebody/files/gallery_body
            var file: File = File(filedir)
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    //EXCEPTION 디렉토리가 만들어지지 않음
                }
            }

            assetsToExternalStorage()   //assets에 있는 테스트용 이미지를 외부저장소에 복사

            for (f in file.listFiles()) {
                //TODO 이미지 파일이 아닌경우 예외처리
                //TODO 이미지를 암호화해서 저장해놓고 불러올 때만 복호화 하기
                photoList.add(Photo(f))
            }

            if(photoList.size != 0){    //이미지가 하나도 없는 경우에는 selectedImage를 세팅하지 않음
                selectedImage_gallery.setImageBitmap(photoList[0].getBitmap(measuredWidth, measuredHeight))
                selectedImage_gallery.setTag(0)
            }
            if(photoList.size > 1){ //이미지가 2개 이상일 때 오른쪽 버튼 보이기
                rightButton_gallery.visibility = View.VISIBLE
            }
        } else{
            //EXCEPTION 외부저장소가 마운트되지 않아서 파일을 읽고 쓸 수 없음
        }

        //RecyclerView
        galleryView.hasFixedSize()
        galleryView.adapter = GalleryAdapter(this, photoList)

        //button onclick listener
        leftButton_gallery.setOnClickListener {
            rightButton_gallery.visibility = View.VISIBLE

            var prePosition: Int = (selectedImage_gallery.getTag() as Int) - 1
            if(prePosition == 0){   //이전 사진이 없는 경우
                leftButton_gallery.visibility = View.INVISIBLE
            }

            selectedImage_gallery.setImageBitmap(photoList[prePosition].getBitmap(measuredWidth, measuredHeight))
            selectedImage_gallery.setTag(prePosition)
        }

        rightButton_gallery.setOnClickListener {
            leftButton_gallery.visibility = View.VISIBLE

            var nextPosition: Int = (selectedImage_gallery.getTag() as Int) + 1
            if(nextPosition == photoList.size - 1){  //이후 사진이 없는 경우
                rightButton_gallery.visibility = View.INVISIBLE
            }

            selectedImage_gallery.setImageBitmap(photoList[nextPosition].getBitmap(measuredWidth, measuredHeight))
            selectedImage_gallery.setTag(nextPosition)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_gallery, menu)

        when (googleDriveManager?.checkConnection()) {
            true -> {
                menu.findItem(R.id.action_googleDrive_signInOut_togle).title = getString(R.string.googleDrive_do_signOut)
            }
            false -> {
                menu.findItem(R.id.action_googleDrive_signInOut_togle).title = getString(R.string.googleDrive_do_signIn)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_collage -> {    //콜라주 하러가기
                var intent = Intent(this, CollageActivity::class.java)
                intent.putExtra("photoList", photoList)
                startActivity(intent)
            }

            R.id.action_googleDrive_signInOut_togle -> {//구글드라이브 로그인 토글
                togleItem = item
                when (googleDriveManager?.checkConnection()) {
                    true -> {
                        googleDriveManager?.signOut()
                        Log.d("mydbg_gallery", "do sign out")
                    }
                    false -> {
                        googleDriveManager?.signIn()
                        Log.d("mydbg_gallery", "do sign in")
                    }
                }
            }

            R.id.action_googleDrive_manualSave -> { //구글드라이브 수동 저장
                //googleDriveManager?.saveAllFile()
                googleDriveManager?.upload("${getExternalFilesDir(null)}/gallery_body/front_week4.jpg")
                /*
                intentsender 방식

                val intentsender = googleDriveManager?.upload("${getExternalFilesDir(null)}/gallery_body/front_week4.jpg")
                Log.i("mydbg_gallery","${getExternalFilesDir(null)}/gallery_body/front_week4.jpg upload request")
                if (intentsender != null) {
                    Log.d("mydbg_gallery","$intentsender")
                    try {
                        startIntentSenderForResult(intentsender, 123, null, 0, 0, 0)
                    } catch(e : IntentSender.SendIntentException){
                        e.printStackTrace()
                    }
                    Log.d("mydbg_gallery","upload request failed")
                } else {
                    Log.d("mydbg_gallery", "save file ok")
                }
                */
            }

            R.id.action_googleDrive_manualLoad -> { //구글드라이브 수동 불러오기
                googleDriveManager?.loadAllFile()
                Log.d("mydbg_gallery", "do load file")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        //TODO 비트맵 메모리 반환: 이미지 다시 불러오기
    }
    override fun onStop() {
        super.onStop()
        //TODO 비트맵 메모리 반환: 하드웨어 가속 끄고 비트맵 반환
    }
    fun assetsToExternalStorage() {
        //assets에 있는 파일을 외부저장소로 복사(테스트용)
        for (i in 1..4) {
            var filename: String = "front_week" + i + ".jpg"

            var assetManager: AssetManager = getAssets()
            var input: InputStream = assetManager.open("gallery_body/" + filename)

            var outputfile: String = getExternalFilesDir(null).toString() + "/gallery_body/" + filename
            var output: OutputStream = FileOutputStream(outputfile)

            var buffer: ByteArray = ByteArray(1024)
            var length: Int

            do {
                length = input.read(buffer)
                if (length <= 0) break;
                output.write(buffer, 0, length)
            } while (true)

            output.flush();
            output.close();
            input.close();
        }
    }
}