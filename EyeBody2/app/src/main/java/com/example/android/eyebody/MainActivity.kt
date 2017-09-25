package com.example.android.eyebody

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import io.vrinda.kotlinpermissions.PermissionCallBack
import io.vrinda.kotlinpermissions.PermissionsActivity
import com.example.android.eyebody.dialog.EnterGalleryDialog
import com.example.android.eyebody.init.InitActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : PermissionsActivity() {

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

        // TODO ----- 바탕화면에 toggle icon?? 형식으로 간단하게 클릭만으로 메인 액티비티를 접근할 수 있게 설정하기 (백그라운드?)
        // TODO ----- 메모리 릭 문제 해결 (점점 메모리 사용량이 증가한다.)

        /* lazy initializing (지연 선언)
        해당 변수를 사용하기 바로 직전에 부르기 때문에 처음 실행시 과부하가 적음.
        ***리스너에 넣으면 매번 Intent 함수를 사용하기 때문에 부하가 심할 거 같은데
        이렇게하면 속도가 빨라지는 효과가 있는지는 모르겠다.***
         */
        val cameraPage by lazy { Intent(this, CameraActivity::class.java) }
        val exercisePage by lazy { Intent(this, ExerciseActivity::class.java) }
        val settingPage by lazy { Intent(this, SettingActivity::class.java) }

        /* Listener (이벤트 리스너)
        클릭하면 반응
         */
        fun checkCAMERAPermission(): Boolean {
            var result: Int = ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.CAMERA)
            return result == PackageManager.PERMISSION_GRANTED
        }
        btn_activity_photo.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!checkCAMERAPermission()) {
                    requestPermissions(arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), object : PermissionCallBack {
                        override fun permissionGranted() {
                            super.permissionGranted()
                            Log.v("Camera permissions", "Granted")
                        }
                        override fun permissionDenied() {
                            super.permissionDenied()
                            Log.v("Camera permissions", "Denied")
                        }
                    })
                } else startActivity(cameraPage)
            }
            else startActivity(cameraPage)
        }


        /* SharedPreferences (앱 공유 데이터)
        isUserTypeInitSetting : 유저가 처음 시작할 때 비밀번호, 몸매목표 등을 세팅했는지 확인하는 파일
        MODE_PRIVATE : 다른 앱이 접근 불가(파일 권한 없이 불가를 뜻하는 것 같음) (mode_world_readable : 다른 앱이 공유 데이터에 접근 가능)
         */
        val shared : SharedPreferences = getSharedPreferences("isUserTypeInitSetting", Context.MODE_PRIVATE)

        btn_activity_gallery.setOnClickListener {

            val sharedPref: SharedPreferences = getSharedPreferences("hash-md5", Context.MODE_PRIVATE)
            val isSetPassword = sharedPref.getBoolean("isSetting", false)
            Log.d("mydbg_main", "유저가 gallery 접근을 요청함")

            if (!isSetPassword) {

                Log.d("mydbg_main", "SharedPreferences.isSetting is false or null / hacked or 유저가 앱 실행 중 데이터를 지운 경우")
                Toast.makeText(this, "에러 : 초기비밀번호가 설정되어있지 않습니다.", Toast.LENGTH_LONG).show()

            } else {

                val enterGalleryDialog = EnterGalleryDialog()
                enterGalleryDialog.show(fragmentManager, "enter_gallery")
            }
        }
        btn_activity_func1.setOnClickListener {
            startActivity(exercisePage)
        }
        btn_activity_func2.setOnClickListener {
            startActivity(settingPage)
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
        // TODO ----- 아예 싹 갈아 없고 네비게이션 뷰 구성하기" - fun onCreateOptionMenu 와 onOptionSelected 를 엎어야 함.
        val id by lazy { item.itemId }
        val toast by lazy { Toast.makeText(this, "", Toast.LENGTH_SHORT) }

        when (id) {
            R.id.Actionbar_Backup -> {
                // TODO ----- init 으로 가게 해놓았음
                val initActivityIntent = Intent(this, InitActivity::class.java)
                startActivity(initActivityIntent)
                // TODO ----- intent 전환효과 바꾸기 :: overridePendingTransition(int, int) / xml 파일 같이 쓰면 더 예쁘게 가능.
                // (왜 startActivity 함수 다음에 쓰는 건지 알아봐야 할 거 같음)
                // 사진찍기 같은 경우 드래그로 동그란거 샤악~ ????
                finish()
            }
            R.id.Actionbar_PWmodify -> {
                // TODO ----- 비밀번호 수정에 대하여 새로운 액티비티 구성
                toast.setText("TODO : pw modify")
                toast.show()
            }
            R.id.Actionbar_PWfind -> {
                // TODO ----- 비밀번호 찾기에 대하여 새로운 액티비티 구성
                toast.setText("TODO : pw find")
                toast.show()
            }
            R.id.Actionbar_reDesire -> {
                // TODO ----- 목표 설정에 대하여 새로운 액티비티 구성
                toast.setText("TODO : re Desire")
                toast.show()
            }
            R.id.Actionbar_AlarmSetting -> {
                // TODO ----- 알람 세팅에 대하여 새로운 액티비티 구성
                toast.setText("TODO : Alarm Setting")
                toast.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
