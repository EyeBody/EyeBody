package com.example.android.eyebody.gallery

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.android.eyebody.R
import kotlinx.android.synthetic.main.activity_collage.*

class CollageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collage)

        var photoList: ArrayList<Photo> = intent.getParcelableArrayListExtra("photoList")

        textView.text = "imageURL: " + photoList[0].imageURL + "\nfileName: " + photoList[0].fileName + "\nmemo: " + photoList[0].getMemo()

        //RecyclerView
        //galleryView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        //galleryView.hasFixedSize()
        //galleryView.adapter = GalleryAdapter(this, photoList)
    }
}
