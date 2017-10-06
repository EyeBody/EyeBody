package com.example.android.eyebody.gallery

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
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
