package com.example.android.eyebody.gallery

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.android.eyebody.R
import android.view.Menu
import android.view.MenuItem
import com.kakao.kakaolink.KakaoLink
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder
import java.io.File

class CollageActivity : AppCompatActivity() {
    var photoList: ArrayList<Photo> = ArrayList<Photo>()
    var pos: Int = 0
    var selectedIndexList: ArrayList<Int> = ArrayList<Int>()
    var selectedPhotoList: ArrayList<Photo> = ArrayList<Photo>()
    var imageRatio = "0"

    lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collage)

        photoList = intent.getParcelableArrayListExtra("photoList")
        pos= intent.getIntExtra("pos", 0)

        //ImageSelectFragment 생성
        fragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, ImageSelectFragment())
                .commit()
    }

    override fun onBackPressed() {
        var count: Int = fragmentManager.backStackEntryCount

        if(count == 0){ //스택에 프래그먼트가 없으면 액티비티 뒤로가기
            supportFinishAfterTransition()
            super.onBackPressed()
        } else {
            if(count == 1)
                clearApplicationCache(null) //ImageSelectFragment로 돌아올 때 캐시파일 삭제

            fragmentManager.popBackStack()  //이전 프래그먼트 불러오기
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_image_select, menu)
        this.menu = menu
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

    override fun onDestroy() {
        super.onDestroy()
        clearApplicationCache(null)
    }

    fun clearApplicationCache(file: File?){
        //캐시 파일 삭제
        var dir: File

        if(file == null) dir = cacheDir
        else dir = file

        if(dir == null) return

        var children = dir.listFiles()
        try{
            for(child in children){
                if(child.isDirectory) clearApplicationCache(child)
                else child.delete()
            }
        } catch(e: Exception){

        }
    }
}
