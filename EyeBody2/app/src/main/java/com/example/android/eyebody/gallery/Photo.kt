package com.example.android.eyebody.gallery

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import java.io.*

class Photo: AppCompatActivity, Parcelable {
    var fileUrl: String = ""
    var fileName: String = ""
    var imgWidth: Int = 0
    var imgHeight: Int = 0

    constructor(imgFile: File) {
        fileUrl = imgFile.path //intent로 bitmap이미지를 넘기는 것 보다 url로 넘기는게 좋대서 바꿈
        fileName = imgFile.name //파일이름을 날짜로 저장하고(body20170922190523) 여기서 date정보와 memo 정보를 불러옴

        setImageSize()
    }

    //Parcelable methods
    protected constructor(parcel: Parcel) {
        fileUrl = parcel.readString()
        fileName = parcel.readString()
        imgWidth = parcel.readInt()
        imgHeight = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fileUrl)
        parcel.writeString(fileName)
        parcel.writeInt(imgWidth)
        parcel.writeInt(imgHeight)
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

    fun setImageSize(){
        //원본 이미지 가로세로 크기
        //이미지 크기가 바뀌면 이 함수를 꼭 호출해줘야 함
        var options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(fileUrl, options)

        imgWidth = options.outWidth
        imgHeight = options.outHeight
    }

    fun getBitmap(): Bitmap{
        return getBitmap(imgWidth, imgHeight)
    }

    fun getBitmap(reqWidth: Int, reqHeight: Int): Bitmap{
        //요구되는 이미지 크기(이미지뷰 크기)보다 원래 이미지 크기가 크면 다운사이징
        var sampleSize: Int = 1;    //몇배로 줄일지(가로*세로이므로 제곱배로 줄어듬)

        if(imgWidth > reqWidth || imgHeight > reqHeight){
            val halfWidth = imgWidth / 2
            val halfHeight = imgHeight / 2

            while((halfWidth / sampleSize) >= reqWidth
                    && (halfHeight / sampleSize) >= reqHeight){
                sampleSize *= 2
            }
        }

        //비트맵으로 반환
        var options = BitmapFactory.Options()
        options.inSampleSize = sampleSize

        return BitmapFactory.decodeFile(fileUrl, options) as Bitmap
    }

    fun rotationImage(degree: Float){
        var rotateMatrix = Matrix();
        rotateMatrix.postRotate(degree);

        var rotatedImage = Bitmap.createBitmap(getBitmap(), 0, 0, imgWidth, imgHeight, rotateMatrix, false);

        try {
            var fOut = FileOutputStream(File(fileUrl));
            rotatedImage.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();

            var tmp = imgWidth
            imgWidth = imgHeight
            imgHeight = tmp

        } catch (e: Exception) {
            e.printStackTrace();
        }
    }

    fun copyToCacheDir(context: Context): Photo{
        //편집 중인 이미지를 cacheDir으로 임시 복사
        var newUrl = context.cacheDir.absolutePath + "/" + fileName

        var input: InputStream = FileInputStream(fileUrl)
        var output: OutputStream = FileOutputStream(newUrl)

        var buffer = ByteArray(1024)
        var length: Int

        do {
            length = input.read(buffer)
            if (length <= 0) break;
            output.write(buffer, 0, length)
        } while (true)

        output.flush();
        output.close();
        input.close();

        //복사된 Photo 인스턴스
        return Photo(File(newUrl))
    }

    fun getDate(): String{
        return "2017.00.00"
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