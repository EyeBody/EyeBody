package com.example.android.eyebody.gallery

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.android.eyebody.R
import kotlinx.android.synthetic.main.fragment_image_collage.*
import android.graphics.Bitmap
import android.os.Environment
import android.widget.Toast
import java.io.*


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

        makeGifButton.setOnClickListener {
            val bos = ByteArrayOutputStream()
            val encoder = AnimatedGifEncoder()
            encoder.setDelay(500)
            encoder.setRepeat(0)
            encoder.start(bos)

            try {
                val bmp1: Bitmap
                val bmp2: Bitmap
                val bmp3: Bitmap

                bmp1 = photoList[selected[0]].getBitmap()
                encoder.addFrame(bmp1)
                bmp1.recycle()

                bmp2 = photoList[selected[1]].getBitmap()
                encoder.addFrame(bmp2)
                bmp2.recycle()

                bmp3 = photoList[selected[2]].getBitmap()
                encoder.addFrame(bmp3)
                bmp3.recycle()

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            encoder.finish()

            val filePath = File(Environment.getExternalStorageDirectory().absolutePath + "/EyeBody/sample.gif")
            val outputStream: FileOutputStream
            try {
                if(!filePath.exists()){
                    if(!filePath.mkdirs()){
                        var test = 1
                    }
                }
                outputStream = FileOutputStream(filePath)
                outputStream.write(bos.toByteArray())
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(activity, "오류: " + e.toString(), Toast.LENGTH_SHORT)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(activity, "오류: " + e.toString(), Toast.LENGTH_SHORT)
            }
        }
    }

}
