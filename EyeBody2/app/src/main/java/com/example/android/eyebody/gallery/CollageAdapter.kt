package com.example.android.eyebody.gallery

import android.app.Fragment
import android.content.Context
import android.graphics.Color
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
        //TODO Fragment 종류로 데이터 바인딩 구분하기 if(fragment is ImageSelectFragment){ ... }
        (holder as Item).bindData(photoList[position], position, selectedPhotoList, fragment as ImageSelectFragment)
    }

    class Item(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //ImageSelectFragment에서 데이터 바인딩
        fun bindData(photo: Photo, pos: Int, selectedPhotoList: ArrayList<Int>, fragment: ImageSelectFragment) {
            itemView.imageView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            var measuredWidth = itemView.imageView.getMeasuredWidth();
            var measuredHeight = itemView.imageView.getMeasuredHeight();

            itemView.imageView.setImageBitmap(photo.getImage(measuredWidth, measuredHeight))
            itemView.date.text = photo.getDate()
            fragment.setSelected(itemView, pos, selectedPhotoList.contains(pos))

            itemView.setOnClickListener{
                fragment.setSelected(itemView, pos, !selectedPhotoList.contains(pos))   //없으면(false) 추가해줌(true)
            }
        }
    }
}