package com.example.android.eyebody.gallery

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.android.eyebody.R
import android.view.Menu
import android.view.MenuItem
import com.kakao.kakaolink.KakaoLink
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder

class CollageActivity : AppCompatActivity() {
    var photoList: ArrayList<Photo> = ArrayList<Photo>()
    var selectedPhotoList: ArrayList<Int> = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collage)

        photoList = intent.getParcelableArrayListExtra("photoList")

        //ImageSelectFragment 생성
        fragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, ImageSelectFragment())
                .commit()
    }

    override fun onBackPressed() {
        var count: Int = fragmentManager.backStackEntryCount

        if(count == 0){ //스택에 프래그먼트가 없으면 액티비티 뒤로가기
            super.onBackPressed()
        } else {    //이전 프래그먼트 불러오기
            //TODO 뒤로가기 해도 선택한 이미지 보존
            selectedPhotoList.clear()
            fragmentManager.popBackStack()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_collage, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                ShareKakao(baseContext).shareKakao()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
