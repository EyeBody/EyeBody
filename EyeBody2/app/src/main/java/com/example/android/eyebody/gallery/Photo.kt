package com.example.android.eyebody.gallery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import java.io.*

class Photo(): AppCompatActivity(), Parcelable {
    var imageURL: String = ""
    var fileName: String = ""

    constructor(imgFile: File) : this() {
        imageURL = imgFile.path //intent로 bitmap이미지를 넘기는 것 보다 url로 넘기는게 좋대서 바꿈
        fileName = imgFile.name //파일이름을 날짜로 저장하고(body20170922190523) 여기서 date정보와 memo 정보를 불러옴
    }

    //Parcelable methods
    protected constructor(parcel: Parcel) : this() {
        imageURL = parcel.readString()
        fileName = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageURL)
        parcel.writeString(fileName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Photo> {
        override fun createFromParcel(parcel: Parcel): Photo {
            return Photo(parcel)
        }

        override fun newArray(size: Int): Array<Photo?> {
            return arrayOfNulls(size)
        }
    }

    fun getImage(): Bitmap{
        var img = BitmapFactory.decodeStream(FileInputStream(File(imageURL)))    //File->InputStream->Bitmap
        return img
    }

    fun getDate(): String{
        return "날짜라능"
    }

    fun getMemo(): String{
        //내부 저장소에서 이미지와 같은 이름의 메모 파일 읽기
        var text: String = fileName + "'s memo"

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