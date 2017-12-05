package com.example.android.eyebody.gallery

import android.Manifest
import android.os.Bundle
import android.app.Fragment
import android.content.pm.PackageManager
import com.example.android.eyebody.R
import kotlinx.android.synthetic.main.fragment_image_save.*
import android.graphics.Bitmap
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import java.io.*
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.view.*

class ImageSaveFragment : Fragment() {
    lateinit var collage: CollageActivity
    lateinit var selected: ArrayList<Photo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        collage = activity as CollageActivity
        selected = collage.selectedPhotoList
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_image_save, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_image_save, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_image_save -> {
                //이미지 저장
                Toast.makeText(activity, "저장저장", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //WRITE_EXTERNAL_STORAGE 권한 요청
        //TODO 권한 거부할 때 예외처리
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

        makeGifButton.setOnClickListener {
            saveGif(makeGif())
        }

        horizentalMakeCollageButton.setOnClickListener {
            sampleImageView.setImageBitmap(combineImage(selected, 0))
        }

        verticalMakeCollageButton.setOnClickListener {
            sampleImageView.setImageBitmap(combineImage(selected, 1))
        }

        twoMakeCollageButton.setOnClickListener {
            sampleImageView.setImageBitmap(combineImage(selected, 2))
        }

        threeMakeCollageButton.setOnClickListener {
            sampleImageView.setImageBitmap(combineImage(selected, 3))
        }
    }

    fun makeGif(delay: Int = 500): ByteArrayOutputStream?{
        val bos = ByteArrayOutputStream()
        val encoder = AnimatedGifEncoder()
        encoder.setDelay(delay)
        encoder.setRepeat(0)    //0 미만이면 반복안함, 0 이상이면 반복
        encoder.start(bos)

        try {
            for(photo in selected){
                val bmp: Bitmap = BitmapFactory.decodeStream(FileInputStream(photo.fileUrl))
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

    fun combineImage(photos: ArrayList<Photo>, COLUMNS: Int = 0): Bitmap{
        //TODO 임시폴더에 저장
        //열이 columns개인 바둑판 모양으로 이미지 콜라주
        //이미지는 모두 가로세로 크기가 같음
        //colums가 0 이하 이면 가로로만 이어붙임, 1이면 세로로만 이어붙임(열이 1개)

        var columns = COLUMNS
        var result: Bitmap
        val padding = 15

        if(columns <= 0 || columns > photos.size) //가로로만 이어붙이거나, 사진 갯수가 columns보다 적으면
            columns = photos.size //사진 갯수가 곧 columns

        //comboImage 가로 세로 최대 길이 구하기
        var rows = photos.size / columns
        var remainder = photos.size % columns

        if(remainder != 0) rows += 1 //맨 마지막 줄이 꽉 차지 않은 경우 줄수+1

        var comboWidth = photos[0].imgWidth * columns
        var comboHeight = photos[0].imgHeight * rows

        comboWidth += padding * (columns + 1) //맨 왼쪽, 사진 사이사이, 맨 오른쪽 패딩
        comboHeight += padding * (rows + 1) //맨 위, 사진 사이사이, 맨 밑 패딩

        //비트맵 이미지 생성
        result = Bitmap.createBitmap(comboWidth, comboHeight, Bitmap.Config.ARGB_8888)

        var comboImage = Canvas(result)
        comboImage.drawColor(Color.WHITE)

        var currentWidth: Float = padding.toFloat() //맨 왼쪽 패딩
        var currentHeight: Float = padding.toFloat()    //맨 위쪽 패딩

        for(r in 0..(rows - 1)){    //rows는 0부터 시작
            if(r == (rows - 1) && remainder != 0) //마지막 줄이 꽉 차지 않은 경우
                columns = remainder

            for(c in 0..(columns - 1)){
                var idx = (r * COLUMNS) + c
                comboImage.drawBitmap(photos[idx].getBitmap(), currentWidth, currentHeight, null)   //다음 칸에 이어붙이기
                currentWidth += photos[idx].imgWidth.toFloat() + padding.toFloat()
            }
            currentWidth = padding.toFloat()
            currentHeight += photos[0].imgHeight.toFloat() + padding.toFloat()
        }

        return result
    }
}
