package com.example.android.eyebody.gallery

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.eyebody.R
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.list_gallery_management.view.*


/**
 * Created by yeaji on 2017-09-21.
 */
class GalleryAdapter (var c: Context, var lists: ArrayList<Photo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        var v = LayoutInflater.from(c).inflate(R.layout.list_gallery_management, parent, false)
        return Item(v)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as Item).bindData(lists[position], position)
    }

    class Item(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(photo: Photo, pos: Int) {
            itemView.imageView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            var measuredWidth = itemView.imageView.getMeasuredWidth();
            var measuredHeight = itemView.imageView.getMeasuredHeight();

            itemView.imageView.setImageBitmap(photo.getBitmap(measuredWidth, measuredHeight))
            itemView.date.text = photo.getDate()

            itemView.setOnClickListener{
                var imgView = (itemView.context as GalleryActivity).selectedImage_gallery
                itemView.imageView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                var measuredWidth = itemView.imageView.getMeasuredWidth();
                var measuredHeight = itemView.imageView.getMeasuredHeight();

                imgView.setImageBitmap(photo.getBitmap(measuredWidth, measuredHeight))
                imgView.setTag(pos)

                //좌우 넘기기 버튼 visibility
                (itemView.context as GalleryActivity).leftButton_gallery.visibility = View.VISIBLE
                (itemView.context as GalleryActivity).rightButton_gallery.visibility = View.VISIBLE
                if(pos == 0){
                    (itemView.context as GalleryActivity).leftButton_gallery.visibility = View.INVISIBLE
                }
                if(pos == (itemView.context as GalleryActivity).photoList.size - 1){
                    (itemView.context as GalleryActivity).rightButton_gallery.visibility = View.INVISIBLE
                }
            }
        }
    }
}