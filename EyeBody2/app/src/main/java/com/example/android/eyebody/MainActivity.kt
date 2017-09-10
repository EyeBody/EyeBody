package com.example.android.eyebody

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fun onCreateOptionsMenu(menu: Menu): Boolean {
            super.onCreateOptionsMenu(menu)
            menuInflater.inflate(R.menu.menu_sel, menu)
            return true
        }

        fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id = item.itemId
            if (id == R.id.settingButton) {
                val settingPage = Intent(this, SettingActivity::class.java)
                startActivity(settingPage)
            }
            return super.onOptionsItemSelected(item)
        }
        btn_activity_photo.setOnClickListener {
            val cameraPage = Intent(this, CameraActivity::class.java)
            startActivity(cameraPage)
        }
        btn_activity_gallery.setOnClickListener {
            val galleryPage = Intent(this, GalleryActivity::class.java)
            startActivity(galleryPage)
        }
        btn_activity_func1.setOnClickListener {
            val exercisePage = Intent(this, ExerciseActivity::class.java)
            startActivity(exercisePage)
        }
    }
}
