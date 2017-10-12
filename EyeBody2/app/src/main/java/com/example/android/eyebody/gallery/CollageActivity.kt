package com.example.android.eyebody.gallery

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.android.eyebody.R

class CollageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collage)

        var photoList: ArrayList<Photo> = intent.getParcelableArrayListExtra("photoList")
        var selectedPhotoList: ArrayList<Int> = ArrayList<Int>()

        //ImageSelectFragment
        var imageSelectFragment = ImageSelectFragment()
        var bundle = Bundle()
        bundle.putParcelableArrayList("photoList", photoList)
        bundle.putIntegerArrayList("selectedPhotoList", selectedPhotoList)
        imageSelectFragment.arguments = bundle

        var fragmentTransaction  = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container, imageSelectFragment).commit()
    }
}
