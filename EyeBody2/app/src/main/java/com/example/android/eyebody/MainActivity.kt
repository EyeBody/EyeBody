package com.example.android.eyebody

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
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
            if (id == R.id.btn_activity_func2) {
                val settingPage = Intent(this, SettingActivity::class.java)
                startActivity(settingPage)
            }
            return super.onOptionsItemSelected(item)
        }

        /* lazy initializing (지연 선언)
        해당 변수를 사용하기 바로 직전에 부르기 때문에 처음 실행시 과부하가 적음.

        ***리스너에 넣으면 매번 Intent 함수를 사용하기 때문에 부하가 심할 거 같은데
        이렇게하면 속도가 빨라지는 효과가 있는지는 모르겠다.***
         */
        val cameraPage      by lazy {   Intent(this, CameraActivity::class.java)    }
        val galleryPage     by lazy {   Intent(this, GalleryActivity::class.java)   }
        val exercisePage    by lazy {   Intent(this, ExerciseActivity::class.java)  }

        /* SharedPreferences (앱 공유 데이터)
        isUserTypeInitSetting : 유저가 처음 시작할 때 비밀번호, 몸매목표 등을 세팅했는지 확인하는 파일
        MODE_PRIVATE : 다른 앱이 접근 불가(파일 권한 없이 불가를 뜻하는 것 같음) (mode_world_readable : 다른 앱이 공유 데이터에 접근 가능)
         */
        val shared : SharedPreferences = getSharedPreferences("isUserTypeInitSetting", Context.MODE_PRIVATE)
        // TODO("공유데이터로 initActivity를 실행하게? 아니면 이닛에서 공유데이터를 판별할지 해야함")
        /* Listener (이벤트 리스너)
        클릭하면 반응
         */
        btn_activity_photo.setOnClickListener {
            startActivity(cameraPage)
        }
        btn_activity_gallery.setOnClickListener {
            startActivity(galleryPage)
        }
        btn_activity_func1.setOnClickListener {
            startActivity(exercisePage)
        }
    }


    /* onCreateOptionMenu
    액션바에 옵션메뉴를 띄우게 함. xml 긁어서
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_sel, menu)
        return true
    }


    /* onOptionItemSelected
    옵션메뉴에서 아이템이 선택됐을 때 발생하는 이벤트
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id      by lazy {   item.itemId     }
        val toast   by lazy {   Toast.makeText(this, "", Toast.LENGTH_SHORT)    }


        when (id) {
            R.id.Actionbar_Backup -> {
                val settingPage=Intent(this,SettingActivity::class.java)
                startActivity(settingPage)
            }
            R.id.Actionbar_PWmodify -> {
                toast.setText("TODO : pw modify")
                toast.show()
            }
            R.id.Actionbar_PWfind -> {
                toast.setText("TODO : pw find")
                toast.show()
            }
            R.id.Actionbar_reDesire -> {
                toast.setText("TODO : re Desire")
                toast.show()
            }
            R.id.Actionbar_AlarmSetting -> {
                toast.setText("TODO : Alarm Setting")
                toast.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
