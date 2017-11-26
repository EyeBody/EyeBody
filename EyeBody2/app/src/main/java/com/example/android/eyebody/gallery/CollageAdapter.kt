package com.example.android.eyebody.gallery

import android.app.Fragment
import android.content.Context
<<<<<<< HEAD
import android.graphics.Color
=======
>>>>>>> gallery
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.eyebody.R
import kotlinx.android.synthetic.main.list_gallery.view.*

class CollageAdapter (var context: Context, var photoList: ArrayList<Photo>, var selectedPhotoList: ArrayList<Int>, var fragment: Fragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        var v = LayoutInflater.from(context).inflate(R.layout.list_collage, parent, false)
        return Item(v)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
<<<<<<< HEAD
        //TODO Fragment 종류로 데이터 바인딩 구분하기 if(fragment is ImageSelectFragment){ ... }
=======
>>>>>>> gallery
        (holder as Item).bindData(photoList[position], position, selectedPhotoList, fragment as ImageSelectFragment)
    }

    class Item(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //ImageSelectFragment에서 데이터 바인딩
        fun bindData(photo: Photo, pos: Int, selectedPhotoList: ArrayList<Int>, fragment: ImageSelectFragment) {
<<<<<<< HEAD
            itemView.imageView.setImageBitmap(photo.getImage())
            itemView.date.text = photo.getDate()

            itemView.setOnClickListener{
                itemView.isSelected = !itemView.isSelected

                if(itemView.isSelected){
                    itemView.setBackgroundColor(Color.BLUE) //selectedPhotoList의 size로 순서 알 수 있음
                    selectedPhotoList.add(pos)

                    if(selectedPhotoList.size == 1){    //선택한 이미지가 하나 이상일 때 이미지편집 메뉴 아이콘 보여주기
                        fragment.actionEditImage_setVisible(true)
                    }
                }else{
                    itemView.setBackgroundColor(Color.TRANSPARENT)
                    selectedPhotoList.remove(pos)

                    if(selectedPhotoList.size == 0){    //선택한 이미지가 하나도 없을 때 이미지편집 메뉴 아이콘 숨기기
                        fragment.actionEditImage_setVisible(false)
                    }
                }
=======
            itemView.imageView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            var measuredWidth = itemView.imageView.measuredWidth
            var measuredHeight = itemView.imageView.measuredHeight

            itemView.imageView.setImageBitmap(photo.getBitmap(measuredWidth, measuredHeight))
            itemView.date.text = photo.getDate()
            fragment.setSelected(itemView, pos, selectedPhotoList.contains(pos))

            itemView.setOnClickListener{
                fragment.setSelected(itemView, pos, !selectedPhotoList.contains(pos))   //없으면(false) 추가해줌(true)
>>>>>>> gallery
            }
        }
    }
}