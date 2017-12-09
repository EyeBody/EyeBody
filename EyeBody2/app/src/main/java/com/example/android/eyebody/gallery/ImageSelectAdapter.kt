package com.example.android.eyebody.gallery

import android.app.Fragment
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.eyebody.R
import kotlinx.android.synthetic.main.list_gallery.view.*

class ImageSelectAdapter(var context: Context, var photoList: ArrayList<Photo>, var selectedIndexList: ArrayList<Int>, var fragment: Fragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        var v = LayoutInflater.from(context).inflate(R.layout.list_image_select, parent, false)
        return Item(v)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as Item).bindData(photoList[position], position, selectedIndexList, fragment as ImageSelectFragment, this)
    }

    class Item(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //ImageSelectFragment에서 데이터 바인딩
        fun bindData(photo: Photo, pos: Int, selectedIndexList: ArrayList<Int>, fragment: ImageSelectFragment, adapter: ImageSelectAdapter) {
            itemView.imageView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            var measuredWidth = itemView.imageView.measuredWidth
            var measuredHeight = itemView.imageView.measuredHeight

            itemView.imageView.setImageBitmap(photo.getBitmap(measuredWidth, measuredHeight))
            itemView.date.text = photo.getDate()
            fragment.setSelected(itemView, pos, selectedIndexList.contains(pos))

            itemView.setOnClickListener{
                fragment.setSelected(itemView, pos, !selectedIndexList.contains(pos))   //없으면(false) 추가해줌(true)
            }
        }
    }
}