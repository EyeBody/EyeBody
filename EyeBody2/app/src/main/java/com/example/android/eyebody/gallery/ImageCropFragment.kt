package com.example.android.eyebody.gallery

import android.app.Fragment
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.example.android.eyebody.R
import com.naver.android.helloyako.imagecrop.view.ImageCropView
import kotlinx.android.synthetic.main.fragment_image_crop.*
import java.io.File
import java.io.FileOutputStream

class ImageCropFragment : Fragment() {
    lateinit var collage: CollageActivity
    lateinit var photo: Photo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        collage = activity as CollageActivity
        //TODO 이미지 편집을 다시 할 땐 원본 이미지를 불러와야함
        //TODO 이미지 비율 고정, 같은 크기로 리사이징(이건 collage에서 해야할듯)
        //TODO 이전 이미지로 오버레이
        photo = collage.selectedPhotoList[arguments.getInt("idx")]
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
        var f = File(photo.fileUrl)
        var fos = FileOutputStream(f)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.close()
    }
}
