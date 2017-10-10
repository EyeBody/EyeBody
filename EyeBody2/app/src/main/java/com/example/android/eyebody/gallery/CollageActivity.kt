package com.example.android.eyebody.gallery

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
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

        //TODO 선택한 이미지들로 콜라주 만들기
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_collage, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                //TODO 카카오톡 공유하기
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
