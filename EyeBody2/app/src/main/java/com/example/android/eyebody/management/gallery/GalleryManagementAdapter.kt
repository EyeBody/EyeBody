package com.example.android.eyebody.management.gallery

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.eyebody.R
import com.example.android.eyebody.gallery.Photo
import kotlinx.android.synthetic.main.list_gallery_management.view.*

/**
 * Created by yeaji on 2017-12-23.
 */
class GalleryManagementAdapter(var context: Context, var fragment: GalleryManagementFragment, var lists: ArrayList<Photo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        var v = LayoutInflater.from(context).inflate(R.layout.list_gallery_management, parent, false)
        return Item(v)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as Item).bindData(fragment, lists[position], position)
    }

    class Item(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(fragment: GalleryManagementFragment, photo: Photo, pos: Int) {
            itemView.imageView.setImageBitmap(photo.getBitmap(itemView.imageView))  //리사이징한 이미지
            itemView.date.text = photo.getDate()    //날짜

            itemView.setOnClickListener{
                fragment.itemViewClicked(itemView, pos)
            }

            itemView.setOnLongClickListener {
                fragment.itemViewLongClicked(itemView, pos)
            }
        }
    }
}