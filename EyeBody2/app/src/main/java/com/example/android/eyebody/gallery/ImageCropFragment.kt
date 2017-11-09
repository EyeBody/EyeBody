package com.example.android.eyebody.gallery

import android.app.Fragment
import android.os.Bundle
import android.view.*

import com.example.android.eyebody.R
import kotlinx.android.synthetic.main.fragment_image_crop.*

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
                collage.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cropImageView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        cropImageView.setImageBitmap(photo.getBitmap(cropImageView.measuredWidth, cropImageView.measuredHeight))
    }
}
