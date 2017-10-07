package com.example.android.eyebody.gallery

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.eyebody.R
import kotlinx.android.synthetic.main.list_gallery.view.*

class CollageAdapter (var c: Context, var lists: ArrayList<Photo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        var v = LayoutInflater.from(c).inflate(R.layout.list_collage, parent, false)
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
            itemView.imageView.setImageBitmap(photo.getImage())
            itemView.date.text = photo.getDate()

            itemView.setOnClickListener{
                itemView.isSelected = !itemView.isSelected

                if(itemView.isSelected){
                    itemView.setBackgroundColor(Color.BLUE)
                }else{
                    itemView.setBackgroundColor(Color.TRANSPARENT)
                }
            }
        }
    }
}