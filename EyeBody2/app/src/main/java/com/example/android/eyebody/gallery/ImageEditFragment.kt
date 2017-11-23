package com.example.android.eyebody.gallery

import android.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.*
import com.example.android.eyebody.R
import kotlinx.android.synthetic.main.fragment_image_edit.*

class ImageEditFragment : Fragment() {
    lateinit var photoList: ArrayList<Photo>
    lateinit var selected: ArrayList<Int>
    lateinit var collage: CollageActivity
    var imgViewWidth: Int = 0
    var imgViewHeight: Int = 0
    var currentImageIndex: Int = 0

    //TODO 편집된 이미지는 따로 저장해야 됨
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        collage = activity as CollageActivity
        photoList = collage.photoList
        selected = collage.selectedPhotoList
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_image_edit, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //selectedImageView 가로 세로 크기
        selectedImage_edit.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        imgViewWidth = selectedImage_edit.measuredWidth
        imgViewHeight = selectedImage_edit.measuredHeight

        setImage(currentImageIndex) //이미지 편집(크롭, 스티커)하다가 다시 돌아왔을 때 해당 이미지 바로 보여주기, 초기값 0

        leftButton_edit.setOnClickListener {
            setImage(currentImageIndex - 1)
        }

        rightButton_edit.setOnClickListener {
            setImage(currentImageIndex + 1)
        }

        cropButton.setOnClickListener {
            //ImageCropFragment로 교체
            var imageCropFragment = ImageCropFragment()
            var bundle = Bundle()

            bundle.putInt("idx", selected[currentImageIndex])
            imageCropFragment.arguments = bundle

            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, imageCropFragment)
                    .addToBackStack(null)
                    .commit()
        }

        rotationButton.setOnClickListener {
            var idx: Int = selected[currentImageIndex]
            photoList[idx].rotationImage(90f)
            selectedImage_edit.setImageBitmap(photoList[idx].getBitmap(imgViewWidth, imgViewHeight))
        }

        stickerButton.setOnClickListener {
            //스티커 붙이기
            //https://github.com/niravkalola/Android-StickerView
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

    fun setImage(pos: Int){
        try {
            //좌우 넘기기 버튼
            leftButton_edit.visibility = View.VISIBLE
            rightButton_edit.visibility = View.VISIBLE

            if (pos == 0) {   //이전 사진이 없는 경우
                leftButton_edit.visibility = View.INVISIBLE
            }

            if (pos == selected.size - 1) {  //이후 사진이 없는 경우
                rightButton_edit.visibility = View.INVISIBLE
            }

            //이미지와 현재 인덱스 변경
            selectedImage_edit.setImageBitmap(photoList[selected[pos]].getBitmap(imgViewWidth, imgViewHeight))
            imageIndexTextView.text = (pos + 1).toString() + "/" + selected.size
            currentImageIndex = pos
        } catch (e: Exception){
            Log.e("ImageEditFragment", "out of index")  //버튼 빠르게 누르면 OutOfIndex 에러 발생
        }
    }
}