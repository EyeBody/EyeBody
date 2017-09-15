package com.example.android.eyebody

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_gallery.*
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import android.widget.Toast
import java.io.*

/*
이미지 암호화
file IO stream으로 이미지 AES 암호화/복호화
또는 파일 확장자 변환

갤러리 UI도 만들어야 함
 */
class GalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        //외부저장소(SD카드)가 마운트되었는지 확인
        //디렉토리 생성
        //파일 생성
        //파일 쓰기

        /*
        var state: String = Environment.getExternalStorageState()
        if(Environment.MEDIA_MOUNTED.equals(state)){
            var filedir: String = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/gallery_body"
            textView.text = filedir

            var file: File? = File(filedir, "eyebody.txt")
            Toast.makeText(this, "디렉토리가 만들어짐", Toast.LENGTH_SHORT).show()
            if (!file!!.mkdirs()) {
                //Log.e(LOG_TAG, "Directory not created")
                Toast.makeText(this, "디렉토리가 만들어지지 않음", Toast.LENGTH_SHORT).show()
            }

            //파일 쓰기
            file = File(filedir, "eyebody.txt")
            try{
                var fos: FileOutputStream = FileOutputStream(file)
                fos.write("eyebododody".toByteArray())
                fos.flush()
                fos.close()
            } catch (e: Exception){
                e.printStackTrace()
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }
        }*/
    }

    fun getImageFromAssets(){
        //assets에서 이미지 불러오기
        var assetManager: AssetManager = getAssets()  //AssetManager 생성
        var inputStream: InputStream = assetManager.open("gallery_body/front_week1.jpg")    //InputStream으로 변환
        var bitmapImg: Bitmap = BitmapFactory.decodeStream(inputStream)  //Bitmap으로 변환
        imageView.setImageBitmap(bitmapImg)   //Bitmap까지 변환해야 이미지뷰에 띄울 수 있음
    }

    fun InternalFileIO(){
        //내부 저장소에 파일 읽고쓰기
        var fileName: String = "eyebody.txt"

        btn_encrypt.setOnClickListener{
            //내부저장소에 파일 쓰기
            var string: String = et_sample.text.toString()  //샘플텍스트

            try{
                var outputStream: FileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
                outputStream.write(string.toByteArray())
                outputStream.close()
                Toast.makeText(this, getFilesDir().toString() + "쓰기 완료", Toast.LENGTH_SHORT).show()
            } catch(e: Exception){
                e.printStackTrace()
                Toast.makeText(this, "쓰기 실패", Toast.LENGTH_SHORT).show()
            }
        }

        btn_decrypt.setOnClickListener{
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
                textView.setText(text)
            } catch(e: FileNotFoundException){
                e.printStackTrace()
                Toast.makeText(this, "파일을 찾을 수 없습니다", Toast.LENGTH_LONG).show()
            } catch(e: Exception){
                e.printStackTrace()
                //Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}