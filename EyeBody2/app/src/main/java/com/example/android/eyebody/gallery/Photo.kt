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
        //TODO 이미지 리사이징
        //https://medium.com/marojuns-android/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C%EC%97%90%EC%84%9C%EC%9D%98-%EB%B9%84%ED%8A%B8%EB%A7%B5%EA%B3%BC-%EB%A9%94%EB%AA%A8%EB%A6%AC%EC%9D%98-%EC%83%81%EA%B4%80%EA%B4%80%EA%B3%84-125308c293b9
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