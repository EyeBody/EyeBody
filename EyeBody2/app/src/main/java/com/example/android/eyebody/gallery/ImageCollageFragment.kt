package com.example.android.eyebody.gallery

import android.Manifest
import android.os.Bundle
import android.app.Fragment
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.android.eyebody.R
import kotlinx.android.synthetic.main.fragment_image_collage.*
import android.graphics.Bitmap
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import java.io.*
import android.graphics.BitmapFactory




class ImageCollageFragment : Fragment() {
    lateinit var collage: CollageActivity
    lateinit var photoList: ArrayList<Photo>
    lateinit var selected: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        collage = activity as CollageActivity
        photoList = collage.photoList
        selected = collage.selectedPhotoList
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_image_collage, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //WRITE_EXTERNAL_STORAGE 권한 요청
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

        makeGifButton.setOnClickListener {
            makeGif()
        }
    }

    fun makeGif(){
        val bos = ByteArrayOutputStream()
        val encoder = AnimatedGifEncoder()
        encoder.setDelay(500)
        encoder.setRepeat(0)
        encoder.start(bos)

        try {
            val bmp1: Bitmap
            val bmp2: Bitmap
            val bmp3: Bitmap

            bmp1 = BitmapFactory.decodeStream(FileInputStream(photoList[selected[0]].imageURL))
            encoder.addFrame(bmp1)
            bmp1.recycle()

            bmp2 = BitmapFactory.decodeStream(FileInputStream(photoList[selected[1]].imageURL))
            encoder.addFrame(bmp2)
            bmp2.recycle()

            bmp3 = BitmapFactory.decodeStream(FileInputStream(photoList[selected[2]].imageURL))
            encoder.addFrame(bmp3)
            bmp3.recycle()

        } catch (e: FileNotFoundException) {
            Toast.makeText(activity, "GIF 파일을 만들지 못하였습니다", Toast.LENGTH_SHORT).show()
            return
        }


        encoder.finish()


        //폴더 만들기(외부저장소/Pictures/EyeBody)
        var filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/EyeBody"
        var file = File(filePath)

        if(!file.exists()){
            if(!file.mkdirs()){
                Toast.makeText(activity, "디렉토리가 만들어지지 않음", Toast.LENGTH_SHORT).show()
                return
            }
        }

        //파일 만들기
        file = File(filePath, "sample.gif")
        try {
            var fos = FileOutputStream(file)
            fos.write(bos.toByteArray())
            fos.close()
        } catch (e: FileNotFoundException) {
            Toast.makeText(activity, "파일을 찾을 수 없습니다", Toast.LENGTH_SHORT).show()
            return
        } catch (e: IOException) {
            Toast.makeText(activity, "쓰기 오류", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(activity, "GIF 이미지를 저장하였습니다", Toast.LENGTH_SHORT).show()
    }

}
