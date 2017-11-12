package com.example.android.eyebody.camera

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import com.example.android.eyebody.R
import com.example.android.eyebody.R.id.imageView
import com.example.android.eyebody.gallery.Photo
import kotlinx.android.synthetic.main.activity_gallery.*
import java.io.File

class StickerActivity : AppCompatActivity() {
    var listView: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        listView = this.findViewById(R.id.stickerList)

        var items: ArrayList<String> = ArrayList()
        items.add("1")
        items.add("2")
        items.add("3")
        items.add("4")
        items.add("5")

       // var adapter=StickerAdapter(this,0,items)
       // listView?.adapter=adapter
    }
}

