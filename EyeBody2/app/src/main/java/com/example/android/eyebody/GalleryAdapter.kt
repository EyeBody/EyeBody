package com.example.android.eyebody

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.list_gallery.view.*


/**
 * Created by yeaji on 2017-09-21.
 */
class GalleryAdapter (var c: Context, var lists: ArrayList<Photo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        var v = LayoutInflater.from(c).inflate(R.layout.list_gallery, parent, false)
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
            itemView.imageView.setImageBitmap(photo.image)
            itemView.date.text = photo.date

            itemView.setOnClickListener{
                var imgView: ImageView = (itemView.context as Activity).findViewById(R.id.selectedImage)
                imgView.setImageBitmap(photo.image)
                imgView.setTag(pos)
            }
        }
    }
}