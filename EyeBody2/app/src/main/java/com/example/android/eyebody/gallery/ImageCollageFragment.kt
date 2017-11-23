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
import android.graphics.Canvas
import android.graphics.Color

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
            saveGif(makeGif())
        }

        makeCollageButton.setOnClickListener {
            sampleImageView.setImageBitmap(combineImage(photoList, selected))
        }
    }

    fun makeGif(delay: Int = 500): ByteArrayOutputStream?{
        val bos = ByteArrayOutputStream()
        val encoder = AnimatedGifEncoder()
        encoder.setDelay(delay)
        encoder.setRepeat(0)    //0 미만이면 반복안함, 0 이상이면 반복
        encoder.start(bos)

        try {
            for(sel in selected){
                val bmp: Bitmap = BitmapFactory.decodeStream(FileInputStream(photoList[sel].imageURL))
                encoder.addFrame(bmp)
                bmp.recycle()
            }
        } catch (e: FileNotFoundException) {
            Toast.makeText(activity, "GIF 파일을 만들지 못하였습니다", Toast.LENGTH_SHORT).show()
            return null
        }

        encoder.finish()

        return bos
    }

    fun saveGif(bos: ByteArrayOutputStream?){
        if(bos == null) return

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
        file = File(filePath, "sample.gif") //TODO 파일 이름 설정
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

    fun combineImage(photoList: ArrayList<Photo>, selected: ArrayList<Int>, columns: Int = 0): Bitmap{
        //열이 columns개인 바둑판 모양으로 이미지 콜라주
        //colums <= 0이면 가로로만 이어붙임, 1이면 세로로만 이어붙임(열이 1개)
        // 0 1 2 <- 열(columns)은 3개
        // 3 4 5
        // 6 7 ...

        var columns = columns
        var result: Bitmap
        val padding = 15

        if(columns <= 0 || columns > selected.size) //가로로만 이어붙이거나, 사진 갯수가 columns보다 적으면
            columns = selected.size //사진 갯수가 곧 columns

        //comboImage 가로 세로 최대 길이 구하기
        var width: Int = padding * (columns + 1) //맨 왼쪽, 사진 사이사이, 맨 오른쪽 패딩
        var height: Int = padding * 2 //위 아래 패딩

        //TODO 이미지 크기가 모두 같다면 이런 뻘짓 안해도 됨
        val rows = selected.size / columns  //0부터 카운팅
        val remainder = selected.size % columns

        if(remainder == 0) rows - 1

        for(r in 0..rows){
            if(r == rows && remainder != 0) columns = remainder //마지막 줄

           var rowWidth = 0
            var rowHeight = 0

            for(c in 0..(columns - 1)){
                var w = photoList[selected[(r*c) + c]].imgWidth
                var h = photoList[selected[(r*c) + c]].imgHeight

                rowWidth += w
                if(rowHeight < h) rowHeight = h
            }

            if(width < rowWidth)  width = rowWidth
            height += rowHeight
        }


//        var totalWidth = padding * (selected.size + 1)  //사진 사이사이 패딩
//        var maxHeight = 0
//
//        for(idx in selected){
//            totalWidth += photoList[idx].imgWidth
//            if(maxHeight < photoList[idx].imgHeight)
//                maxHeight = photoList[idx].imgHeight
//        }
//        maxHeight += padding * 2 //위 아래 패딩

        result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        var comboImage = Canvas(result)
        comboImage.drawColor(Color.WHITE)

        var currentWidth: Float = padding.toFloat() //맨 왼쪽 패딩
        var currentHeight: Float = padding.toFloat()    //맨 위쪽 패딩

//        for(idx in selected){
//            comboImage.drawBitmap(photoList[idx].getBitmap(), currentWidth, currentHeight, null)   //오른쪽에 이어붙이기
//            currentWidth += photoList[idx].imgWidth.toFloat() + padding.toFloat()
//        }

        for(r in 0..rows){
            if(r == rows && remainder != 0) columns = remainder //마지막 줄
            var rowHeight = 0

            for(c in 0..(columns - 1)){
                var photo = photoList[selected[(r*c) + c]]
                var h = photo.imgHeight

                comboImage.drawBitmap(photo.getBitmap(), currentWidth, currentHeight, null)   //다음 칸에 이어붙이기
                currentWidth += photo.imgWidth.toFloat() + padding.toFloat()
                if(rowHeight < h) rowHeight = h
            }

            currentHeight += rowHeight + padding
        }

        return result
    }
}
