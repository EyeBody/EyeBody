package com.example.android.eyebody.gallery

import android.app.Fragment
import android.os.Bundle
import android.view.*
import com.example.android.eyebody.R
import kotlinx.android.synthetic.main.fragment_image_edit.*

//https://github.com/niravkalola/Android-StickerView
class ImageEditFragment : Fragment() {
    lateinit var photoList: ArrayList<Photo>
    lateinit var selected: ArrayList<Int>
    lateinit var collage: CollageActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        collage = activity as CollageActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater.inflate(R.layout.fragment_image_edit, container, false)
        photoList = (activity as CollageActivity).photoList
        selected = (activity as CollageActivity).selectedPhotoList

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedImage_edit.setImageBitmap(photoList[selected[0]].getImage())
        selectedImage_edit.setTag(0)

        if(selected.size > 1){    //사진이 하나 이상인 경우
            rightButton_edit.visibility = View.VISIBLE
        }

        imageIndexTextView.text = "1/" + selected.size

        leftButton_edit.setOnClickListener {
            rightButton_edit.visibility = View.VISIBLE

            var prePosition: Int = (selectedImage_edit.getTag() as Int) - 1
            if(prePosition == 0){   //이전 사진이 없는 경우
                leftButton_edit.visibility = View.INVISIBLE
            }

            selectedImage_edit.setImageBitmap(photoList[selected[prePosition]].getImage())
            selectedImage_edit.setTag(prePosition)

            imageIndexTextView.text = (prePosition + 1).toString() + "/" + selected.size
        }

        rightButton_edit.setOnClickListener {
            leftButton_edit.visibility = View.VISIBLE

            var nextPosition: Int = (selectedImage_edit.getTag() as Int) + 1
            if(nextPosition == selected.size - 1){  //이후 사진이 없는 경우
                rightButton_edit.visibility = View.INVISIBLE
            }

            selectedImage_edit.setImageBitmap(photoList[selected[nextPosition]].getImage())
            selectedImage_edit.setTag(nextPosition)

            imageIndexTextView.text = (nextPosition + 1).toString() + "/" + selected.size
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_image_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_image_collage -> {
                //ImageEditFragment로 교체
/*                var imageEditFragment = ImageCollageFragment()
                var bundle = Bundle()

                bundle.putIntegerArrayList("selectedPhotoList", collage.selectedPhotoList)
                imageEditFragment.arguments = bundle

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, imageEditFragment)
                        .addToBackStack(null)
                        .commit()*/
            }
        }
        return super.onOptionsItemSelected(item)
    }
}