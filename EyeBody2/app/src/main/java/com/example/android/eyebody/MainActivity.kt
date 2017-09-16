package com.example.android.eyebody

import android.content.Intent
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

        // TODO("바탕화면에 toggle icon?? 형식으로 간단하게 클릭만으로 메인 액티비티를 접근할 수 있게 설정하기 (백그라운드?)")

        /* lazy initializing (지연 선언)
        해당 변수를 사용하기 바로 직전에 부르기 때문에 처음 실행시 과부하가 적음.

        ***리스너에 넣으면 매번 Intent 함수를 사용하기 때문에 부하가 심할 거 같은데
        이렇게하면 속도가 빨라지는 효과가 있는지는 모르겠다.***
         */
        val cameraPage      by lazy {   Intent(this, CameraActivity::class.java)    }
        val galleryPage     by lazy {   Intent(this, GalleryActivity::class.java)   }
        val exercisePage    by lazy {   Intent(this, ExerciseActivity::class.java)  }




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
        // TODO("★ 아예 싹 갈아 없고 네비게이션 뷰 구성하기") - fun onCreateOptionMenu 와 onOptionSelected 를 엎어야 함.
        val id      by lazy {   item.itemId     }
        val toast   by lazy {   Toast.makeText(this, "", Toast.LENGTH_SHORT)    }


        when (id) {
            R.id.Actionbar_Backup -> {
                // TODO("init 으로 가게 해놓았음")
                val dd=Intent(this,InitActivity::class.java)
                startActivity(dd)
            }
            R.id.Actionbar_PWmodify -> {
                // TODO("★★ 비밀번호 수정에 대하여 새로운 액티비티 구성")
                toast.setText("TODO : pw modify")
                toast.show()
            }
            R.id.Actionbar_PWfind -> {
                // TODO("★★ 비밀번호 찾기에 대하여 새로운 액티비티 구성")
                toast.setText("TODO : pw find")
                toast.show()
            }
            R.id.Actionbar_reDesire -> {
                // TODO("★★ 목표 설정에 대하여 새로운 액티비티 구성")
                toast.setText("TODO : re Desire")
                toast.show()
            }
            R.id.Actionbar_AlarmSetting -> {
                // TODO("★★ 알람 세팅에 대하여 새로운 액티비티 구성")
                toast.setText("TODO : Alarm Setting")
                toast.show()
            }
        }


        return super.onOptionsItemSelected(item)
    }
}
