package com.example.android.eyebody.gallery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import java.io.*

/**
 * Created by yeaji on 2017-09-22.
 * 갤러리에서 사진 한 장에 담겨있는 정보들
 * 사진 찍은 날짜
 * 사진 데이터
 * 메모
 *
 */
class Photo(imgFile: File): AppCompatActivity() {
    val image: Bitmap = BitmapFactory.decodeStream(FileInputStream(imgFile))   //InputStream->Bitmap
    val date: String = imgFile.name //파일이름을 날짜로 저장하기로 함 ex)body20170922190523
    var memo: String = readMemo(imgFile.name)

    fun readMemo(fileName: String): String{
        //내부 저장소에서 파일 읽기
        var text: String = ""

        try{
            var inputStream: FileInputStream = openFileInput(fileName)
            var buf: BufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var line: String? = ""

            do{
                line = buf.readLine()
                if(line == null) break
                text += line
            } while(true)

            inputStream.close()
        } catch(e: FileNotFoundException){
            e.printStackTrace()
            //Toast.makeText(this, "파일을 찾을 수 없습니다", Toast.LENGTH_LONG).show()
        } catch(e: Exception){
            e.printStackTrace()
        }

        return text
    }

}