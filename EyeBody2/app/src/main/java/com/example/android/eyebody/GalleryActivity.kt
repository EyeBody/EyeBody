package com.example.android.eyebody

import android.app.Activity
import android.content.res.AssetManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_gallery.*
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import java.io.*

//TODO gallery UI 예쁘게 다듬기
//TODO image metadata로 1주전 2주전 정보 표시

class GalleryActivity : AppCompatActivity() {
    var photoList = ArrayList<Photo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        //외부저장소(SD카드)가 마운트되었는지 확인
        var state: String = Environment.getExternalStorageState()
        if(Environment.MEDIA_MOUNTED.equals(state)){
            //디렉토리 생성
            var filedir: String = getExternalFilesDir(null).toString() + "/gallery_body"  //Android/data/com.example.android.eyebody/files/gallery_body
            var file: File = File(filedir)
            if(!file.exists()){
                if(!file.mkdirs()){
                    //디렉토리가 만들어지지 않음
                }
            }
            //테스트용 이미지를 외부저장소에 따로 넣어놔야 함

            for(f in file.listFiles()){
                //TODO 이미지 파일이 아닌경우 예외처리
                //TODO 이미지를 암호화해서 저장해놓고 불러올 때만 복호화 하기
                photoList.add(Photo(f))
            }

            selectedImage.setImageBitmap(photoList[0].image)
            selectedImage.setTag(0)
        } else{
            //외부저장소가 마운트되지 않아서 파일을 읽고 쓸 수 없음
        }

        list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        list.hasFixedSize()
        list.adapter = GalleryAdapter(this, photoList)
    }

    fun showPreviousImage(v: View){
        try {
            var prePosition: Int = (selectedImage.getTag() as Int) - 1
            selectedImage.setImageBitmap(photoList[prePosition].image)
            selectedImage.setTag(prePosition)
        } catch(e: Exception){
            //Toast.makeText(this, "앞이 없음", Toast.LENGTH_SHORT).show()
        }
    }

    fun showNextImage(v: View){
        try {
            var nextPosition: Int = (selectedImage.getTag() as Int) + 1
            selectedImage.setImageBitmap(photoList[nextPosition].image)
            selectedImage.setTag(nextPosition)
        } catch(e: Exception){
            //Toast.makeText(this, "뒤가 없음", Toast.LENGTH_SHORT).show()
        }
    }

    /* 안쓰는 코드들
    fun assetsToExternalStorage(){
        //assets에 있는 파일을 external storage로 옮기기(테스트용)
        var assetManager = getAssets()
        var files: Array<String>? = null

        try{
            files = assetManager.list("")
        } catch (e: Exception) {
            //Log.e("tag", "Failed to get asset file list.", e);
        }

        if(files != null){

            for (filename: String in files){
                try{
                    var input: InputStream? = null
                    var output: OutputStream? = null

                    input = assetManager.open(filename)
                    var outFile: File = File(getExternalFilesDir(null).toString() + "/gallery_body", filename)
                    output = FileOutputStream(outFile)

                    //copyFile(input, output)
                    var buffer: ByteArray = kotlin.ByteArray(1024)
                    var read: Int = 0
                    do{
                        read = input.read(buffer)
                        if(read == -1) break

                        output.write(buffer, 0, read)
                    }while(true)

                    input.close()
                    output.close()
                } catch(e: Exception){

                }
            }
        }
    }

    fun ShowSelectedImage(){
        //외부저장소(SD카드)가 마운트되었는지 확인
        var state: String = Environment.getExternalStorageState()
        if(Environment.MEDIA_MOUNTED.equals(state)){
            //디렉토리 생성
            var filedir: String = getExternalFilesDir(null).toString() + "/gallery_body"  //Android/data/com.example.android.eyebody/files/gallery_body
            var file: File = File(filedir)
            if(!file.exists()){
                if(!file.mkdirs()){
                    //디렉토리가 만들어지지 않음
                }
            }

            var fileList = file.listFiles() //폴더 안에 있는 파일들 리스트

            for(f in fileList){
                var imageView: ImageView = ImageView(this)
                var lp: LayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                imageView.layoutParams = lp

                file = File(filedir, f.name)
                var fis: FileInputStream = FileInputStream(file)   //InputStream으로 변환
                var bitmapImg: Bitmap = BitmapFactory.decodeStream(fis)  //Bitmap으로 변환
                imageView.setImageBitmap(bitmapImg)   //Bitmap까지 변환해야 이미지뷰에 띄울 수 있음

                imageView.setOnClickListener {  //이미지 크게 보여주기
                    imageViewOnClickListener(imageView)
                    //selectedImage.setImageBitmap(bitmapImg)
                    //selectedImage.setImageDrawable(imageView.getDrawable())
                }

                galleryLayout.addView(imageView)
            }
        } else{
            //외부저장소가 마운트되지 않아서 파일을 읽고 쓸 수 없음
        }
    }

    fun imageViewOnClickListener(v: View?){
        var img = v as ImageView
        selectedImage.setImageDrawable(img.getDrawable())
    }

    fun ExternalTextFileIO(){
        //외부저장소(SD카드)가 마운트되었는지 확인
        var state: String = Environment.getExternalStorageState()
        if(Environment.MEDIA_MOUNTED.equals(state)){
            //디렉토리 생성
            var filedir: String = getExternalFilesDir(null).toString() + "/gallery_body"  //Android/data/com.example.android.eyebody/files/gallery_body
            var file: File = File(filedir)
            if(!file.exists()){
                if(!file.mkdirs()){
                    //디렉토리가 만들어지지 않음
                }
            }

            //파일 생성
            file = File(filedir, "eyebody.txt")
            if(!file.createNewFile()){
                //파일이 만들어지지 않음
            }

            //파일 쓰기
            try{
                var str: String = "sample text"
                var content: ByteArray = str.toByteArray()
                var fos: FileOutputStream = FileOutputStream(file)

                fos.write(content)
                fos.flush()
                fos.close()
            } catch(e: Exception){
                e.printStackTrace()
            }

            //파일 읽기
            try {
                var fis: FileInputStream = FileInputStream(file)
                var buffer: BufferedReader = BufferedReader(InputStreamReader(fis))
                var line: String?

                do{
                    line = buffer.readLine()
                    if(line == null) break
                    //textView.text = textView.text.toString() + line
                } while(true)
            } catch(e: Exception){
                e.printStackTrace()
            }
        } else{
            //외부저장소가 마운트되지 않아서 파일을 읽고 쓸 수 없음
        }
    }

    fun getImageFromAssets(){
        //assets에서 이미지 불러오기
        var assetManager: AssetManager = getAssets()  //AssetManager 생성
        var inputStream: InputStream = assetManager.open("gallery_body/front_week1.jpg")    //InputStream으로 변환
        var bitmapImg: Bitmap = BitmapFactory.decodeStream(inputStream)  //Bitmap으로 변환
        //imageView.setImageBitmap(bitmapImg)   //Bitmap까지 변환해야 이미지뷰에 띄울 수 있음
    }

    fun InternalFileIO(){
        //내부 저장소에 파일 읽고쓰기
        var fileName: String = "eyebody.txt"

        //내부저장소에 파일 쓰기
        var string: String = "sample text"

        try{
            var outputStream: FileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
            outputStream.write(string.toByteArray())
            outputStream.close()
            Toast.makeText(this, getFilesDir().toString() + "쓰기 완료", Toast.LENGTH_SHORT).show()
        } catch(e: Exception){
            e.printStackTrace()
            Toast.makeText(this, "쓰기 실패", Toast.LENGTH_SHORT).show()
        }

        //내부 저장소에서 파일 읽기
        try{
            var inputStream: FileInputStream = openFileInput(fileName)
            var buf: BufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var text: String = ""
            var line: String? = ""

            do{
                line = buf.readLine()
                if(line == null) break
                text += line
            } while(true)

            inputStream.close()
            //textView.setText(text)
        } catch(e: FileNotFoundException){
            e.printStackTrace()
            Toast.makeText(this, "파일을 찾을 수 없습니다", Toast.LENGTH_LONG).show()
        } catch(e: Exception){
            e.printStackTrace()
        }
    }
    */
}