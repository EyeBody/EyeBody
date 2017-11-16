package com.example.android.eyebody.gallery

import android.app.Fragment
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.Toast
import com.example.android.eyebody.R
import com.naver.android.helloyako.imagecrop.view.ImageCropView
import kotlinx.android.synthetic.main.fragment_image_crop.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ImageCropFragment : Fragment() {
    lateinit var collage: CollageActivity
    lateinit var photo: Photo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        collage = activity as CollageActivity
        photo = collage.photoList[arguments.getInt("idx")]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image_crop, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_image_crop, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_crop_confirm -> {
                //저장 후 뒤로가기
                if (!imageCropView.isChangingScale) {
                    var b = imageCropView.croppedImage
                    if (b != null) {
                        saveBitmapToFile(b)
                        photo.setImageSize()
                    } else {
                        Toast.makeText(activity, R.string.fail_to_crop, Toast.LENGTH_SHORT).show()
                    }
                }
                collage.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageCropView.setImageBitmap(photo.getBitmap())
        imageCropView.setAspectRatio(9, 16)
        imageCropView.setGridInnerMode(ImageCropView.GRID_ON)
        imageCropView.setGridOuterMode(ImageCropView.GRID_ON)

        overlayGuideButton.setOnClickListener {

        }

        crop1to1Button.setOnClickListener {
            imageCropView.setAspectRatio(1, 1)
        }

        crop3to4Button.setOnClickListener {
            imageCropView.setAspectRatio(3, 4)
        }

        crop9to16Button.setOnClickListener{
            imageCropView.setAspectRatio(9, 16)
        }
    }

    fun saveBitmapToFile(bitmap: Bitmap) {
        //TODO 이렇게 그냥 자르면 원본 이미지가 훼손됨, 따로 저장해야됨
        var f = File(photo.imageURL)
        var fos = FileOutputStream(f)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.close()
    }
}
