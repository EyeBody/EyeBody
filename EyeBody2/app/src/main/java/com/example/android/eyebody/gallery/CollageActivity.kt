package com.example.android.eyebody.gallery

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.example.android.eyebody.R
import kotlinx.android.synthetic.main.activity_collage.*

class CollageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collage)

        var photoList: ArrayList<Photo> = intent.getParcelableArrayListExtra("photoList")

        //RecyclerView
        collageView.hasFixedSize()
        collageView.adapter = CollageAdapter(this, photoList)
    }
}
