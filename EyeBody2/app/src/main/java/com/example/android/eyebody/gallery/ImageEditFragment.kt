package com.example.android.eyebody.gallery

import android.app.Fragment
import android.os.Bundle
import android.util.Log
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
        photoList = collage.photoList
        selected = collage.selectedPhotoList

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedImage_edit.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        var measuredWidth = selectedImage_edit.getMeasuredWidth();
        var measuredHeight = selectedImage_edit.getMeasuredHeight();

        selectedImage_edit.setImageBitmap(photoList[selected[0]].getBitmap(measuredWidth, measuredHeight))
        selectedImage_edit.setTag(0)

        if(selected.size > 1){    //사진이 하나 이상인 경우
            rightButton_edit.visibility = View.VISIBLE
        }

        imageIndexTextView.text = "1/" + selected.size

        leftButton_edit.setOnClickListener {
            try {
                rightButton_edit.visibility = View.VISIBLE

                var prePosition: Int = (selectedImage_edit.getTag() as Int) - 1
                if (prePosition == 0) {   //이전 사진이 없는 경우
                    leftButton_edit.visibility = View.INVISIBLE
                }

                selectedImage_edit.setImageBitmap(photoList[selected[prePosition]].getBitmap(measuredWidth, measuredHeight))
                selectedImage_edit.setTag(prePosition)

                imageIndexTextView.text = (prePosition + 1).toString() + "/" + selected.size
            } catch (e: Exception){
                Log.e("ImageEditFragment", "out of index")  //T버튼 빠르게 누르면 OutOfIndex 에러 발생
            }
        }

        rightButton_edit.setOnClickListener {
            try {
                leftButton_edit.visibility = View.VISIBLE

                var nextPosition: Int = (selectedImage_edit.getTag() as Int) + 1
                if (nextPosition == selected.size - 1) {  //이후 사진이 없는 경우
                    rightButton_edit.visibility = View.INVISIBLE
                }

                selectedImage_edit.setImageBitmap(photoList[selected[nextPosition]].getBitmap(measuredWidth, measuredHeight))
                selectedImage_edit.setTag(nextPosition)

                imageIndexTextView.text = (nextPosition + 1).toString() + "/" + selected.size
            } catch (e: Exception){
                Log.e("ImageEditFragment", "out of index")  //T버튼 빠르게 누르면 OutOfIndex 에러 발생

            }
        }

        cropButton.setOnClickListener {
            //ImageCropFragment로 교체
            var imageCropFragment = ImageCropFragment()
            var bundle = Bundle()

            bundle.putInt("idx", selected[selectedImage_edit.getTag() as Int])
            imageCropFragment.arguments = bundle

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, imageCropFragment)
                    .addToBackStack(null)
                    .commit()
        }

        rotationButton.setOnClickListener {
            //TODO 이미지뷰만 회전하고 나중에 저장
            //selectedImage_edit.setRotation((selectedImage_edit.getRotation() + 90) % 360);
            var idx: Int = selected[selectedImage_edit.getTag() as Int]
            photoList[idx].rotationImage(90f)
            selectedImage_edit.setImageBitmap(photoList[idx].getBitmap(measuredWidth, measuredHeight))
        }

        stickerButton.setOnClickListener {

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_image_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_image_collage -> {
                //회전 저장
                for(idx in selected){
                    //photoList[idx].imageRotation()
                }
                //ImageCollageFragment로 교체
                var imageCollageFragment = ImageCollageFragment()

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, imageCollageFragment)
                        .addToBackStack(null)
                        .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}