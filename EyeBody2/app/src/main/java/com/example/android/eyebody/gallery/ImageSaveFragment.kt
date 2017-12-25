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
import pl.droidsonroids.gif.GifDrawable


class ImageSaveFragment : Fragment() {
    lateinit var collage: CollageActivity
    lateinit var selected: ArrayList<Photo>

    var fileNames: Array<String> = arrayOf("eyebody_collage_horizental.jpg", "eyebody_collage_vertical.jpg", "eyebody_collage_2lines.jpg", "eyebody_collage_3lines.jpg", "eyebody_collage_gif.gif")
    var collageNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        collage = activity as CollageActivity
        selected = collage.selectedPhotoList

        saveGif(makeGif())
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
                saveImage() //이미지 저장
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

        sampleImageView.setImageBitmap(combineImage(selected, collageNumber))

        horizentalMakeCollageButton.setOnClickListener {
            collageNumber = 0
            sampleImageView.setImageBitmap(combineImage(selected, collageNumber))
        }

        verticalMakeCollageButton.setOnClickListener {
            collageNumber = 1
            sampleImageView.setImageBitmap(combineImage(selected, collageNumber))
        }

        twoMakeCollageButton.setOnClickListener {
            collageNumber = 2
            sampleImageView.setImageBitmap(combineImage(selected, collageNumber))
        }

        threeMakeCollageButton.setOnClickListener {
            collageNumber = 3
            sampleImageView.setImageBitmap(combineImage(selected, collageNumber))
        }

        makeGifButton.setOnClickListener {
            collageNumber = 4
            var gifFile = File(activity.cacheDir.absolutePath, fileNames[collageNumber])
            var gifFromFile = GifDrawable(gifFile)
            sampleImageView.setImageDrawable(gifFromFile)
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

        var filePath = activity.cacheDir.absolutePath

        var file = File(filePath)
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Toast.makeText(activity, "디렉토리가 만들어지지 않음", Toast.LENGTH_SHORT).show()
                return
            }
        }

        //파일 만들기
        file = File(filePath, fileNames[4])
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
    }

    fun saveImage(){
        try {
            var path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/EyeBody"
            var dir = File(path)

            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Toast.makeText(activity, "디렉토리가 만들어지지 않음", Toast.LENGTH_SHORT).show()
                    return
                }
            }

            if(collageNumber == 4){ //gif 파일 복사
                var inputfile = activity.cacheDir.absolutePath + "/" + fileNames[collageNumber]
                var input: InputStream = FileInputStream(inputfile)

                var outputfile: String = path + "/" + fileNames[collageNumber]
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
            } else{ //jpeg 파일 저장
                var img = combineImage(selected, collageNumber)
                var imgFile = File(path, fileNames[collageNumber])
                var fos = FileOutputStream(imgFile)
                img.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()
            }
            Toast.makeText(activity, path + "에 " + fileNames[collageNumber] + "로 저장되었습니다", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(activity, "이미지를 저장하지 못했습니다", Toast.LENGTH_SHORT).show()
        }
    }

    fun combineImage(photos: ArrayList<Photo>, COLUMNS: Int = 0): Bitmap{
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
