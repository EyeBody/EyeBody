package com.example.android.eyebody.gallery

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Menu
import com.example.android.eyebody.R
import kotlinx.android.synthetic.main.list_gallery.view.*

class CollageAdapter (var context: Context, var photoList: ArrayList<Photo>, var selectedPhotoList: ArrayList<Int>, var menu: Menu?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        var v = LayoutInflater.from(context).inflate(R.layout.list_collage, parent, false)
        return Item(v)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as Item).bindData(photoList[position], position, selectedPhotoList, menu)
    }

    class Item(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(photo: Photo, pos: Int, selectedPhotoList: ArrayList<Int>, menu: Menu?) {
            itemView.imageView.setImageBitmap(photo.getImage())
            itemView.date.text = photo.getDate()

            itemView.setOnClickListener{
                itemView.isSelected = !itemView.isSelected

                if(itemView.isSelected){
                    itemView.setBackgroundColor(Color.BLUE)
                    selectedPhotoList.add(pos)

                    if(selectedPhotoList.size == 1){    //이미지편집 메뉴 아이콘 보여주기
                        //menu.findItem(R.id.action_editImage).setVisible(true)
                    }
                }else{
                    itemView.setBackgroundColor(Color.TRANSPARENT)
                    selectedPhotoList.remove(pos)

                    if(selectedPhotoList.size == 0){    //이미지편집 메뉴 아이콘 숨기기
                        //menu.findItem(R.id.action_editImage).setVisible(true)
                    }
                }
            }
        }
    }
}