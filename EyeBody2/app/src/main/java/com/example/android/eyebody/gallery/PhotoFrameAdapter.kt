package com.example.android.eyebody.gallery

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.android.eyebody.R
import com.example.android.eyebody.gallery.Photo
import kotlinx.android.synthetic.main.activity_photo_frame.*
import kotlinx.android.synthetic.main.list_gallery_management.view.*

/**
 * Created by yeaji on 2017-12-23.
 */
class PhotoFrameAdapter(var context: PhotoFrameActivity, var lists: ArrayList<Photo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        var v = LayoutInflater.from(context).inflate(R.layout.list_photo_frame, parent, false)
        return Item(v)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as Item).bindData(context, lists[position], position)
    }

    class Item(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(context: PhotoFrameActivity, photo: Photo, pos: Int) {
            itemView.imageView.setImageBitmap(photo.getBitmap(itemView.imageView))  //리사이징한 이미지
        }
    }
}