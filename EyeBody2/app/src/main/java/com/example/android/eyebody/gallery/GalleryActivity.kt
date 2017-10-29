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
import com.example.android.eyebody.googleDriveManage.GoogleDriveManager
import kotlinx.android.synthetic.main.activity_gallery.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class GalleryActivity : AppCompatActivity() {
    var photoList = ArrayList<Photo>()
    var googleDriveManager : GoogleDriveManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        googleDriveManager = GoogleDriveManager(findViewById<View>(android.R.id.content))

        //이미지 불러오기
        var state: String = Environment.getExternalStorageState()   //외부저장소(SD카드)가 마운트되었는지 확인
        if(Environment.MEDIA_MOUNTED.equals(state)){
            //디렉토리 생성
            var filedir: String = getExternalFilesDir(null).toString() + "/gallery_body"  //Android/data/com.example.android.eyebody/files/gallery_body
            var file: File = File(filedir)
            if(!file.exists()){
                if(!file.mkdirs()){
                    //EXCEPTION 디렉토리가 만들어지지 않음
                }
            }

            assetsToExternalStorage()   //assets에 있는 테스트용 이미지를 외부저장소에 복사

            for(f in file.listFiles()){
                //TODO 이미지 파일이 아닌경우 예외처리
                //TODO 이미지를 암호화해서 저장해놓고 불러올 때만 복호화 하기
                photoList.add(Photo(f))
            }

            if(photoList.size != 0){    //이미지가 하나도 없는 경우에는 selectedImage를 세팅하지 않음
                selectedImage_gallery.setImageBitmap(photoList[0].getImage())
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

            selectedImage_gallery.setImageBitmap(photoList[prePosition].getImage())
            selectedImage_gallery.setTag(prePosition)
        }

        rightButton_gallery.setOnClickListener {
            leftButton_gallery.visibility = View.VISIBLE

            var nextPosition: Int = (selectedImage_gallery.getTag() as Int) + 1
            if(nextPosition == photoList.size - 1){  //이후 사진이 없는 경우
                rightButton_gallery.visibility = View.INVISIBLE
            }

            selectedImage_gallery.setImageBitmap(photoList[nextPosition].getImage())
            selectedImage_gallery.setTag(nextPosition)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_gallery, menu)

        when(googleDriveManager?.statusSignInOrOut){
            GoogleDriveManager.SIGN_IN -> {
                menu.findItem(R.id.action_googleDrive_signInOut_togle).title = "Google 로그아웃"
            }
            GoogleDriveManager.SIGN_OUT -> {
                menu.findItem(R.id.action_googleDrive_signInOut_togle).title = "Google 로그인"
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
                when(googleDriveManager?.statusSignInOrOut){
                    GoogleDriveManager.SIGN_IN -> {
                        googleDriveManager?.signOut()
                        Log.d("mydbg_gallery","do sign out")
                        item.title = getString(R.string.googleDrive_do_signIn)
                    }
                    GoogleDriveManager.SIGN_OUT -> {
                        googleDriveManager?.signIn()
                        Log.d("mydbg_gallery","do sign in")
                        if (googleDriveManager?.statusSignInOrOut == GoogleDriveManager.SIGN_IN)
                            item.title = getString(R.string.googleDrive_do_signOut)
                    }
                }
            }

            R.id.action_googleDrive_manualSave -> { //구글드라이브 수동 저장
                googleDriveManager?.saveAllFile()
                Log.d("mydbg_gallery","do save file")
            }

            R.id.action_googleDrive_manualLoad -> { //구글드라이브 수동 불러오기
                googleDriveManager?.loadAllFile()
                Log.d("mydbg_gallery","do load file")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun assetsToExternalStorage(){
        //assets에 있는 파일을 외부저장소로 복사(테스트용)
        for(i in 1..4){
            var filename: String = "front_week" + i + ".jpg"

            var assetManager: AssetManager = getAssets()
            var input: InputStream = assetManager.open("gallery_body/" + filename)

            var outputfile:String = getExternalFilesDir(null).toString() + "/gallery_body/" + filename
            var output: OutputStream = FileOutputStream(outputfile)

            var buffer: ByteArray = ByteArray(1024)
            var length: Int

            do{
                length = input.read(buffer)
                if(length <= 0) break;
                output.write(buffer, 0, length)
            }while(true)

            output.flush();
            output.close();
            input.close();
        }
    }
}