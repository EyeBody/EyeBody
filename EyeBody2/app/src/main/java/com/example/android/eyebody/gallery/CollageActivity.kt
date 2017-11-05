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
    lateinit var menu: Menu

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
            fragmentManager.popBackStack()
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
                shareKakao()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    
    fun shareKakao()
    {
        try{
            val kakaoLink:KakaoLink = KakaoLink.getKakaoLink(this)
            val kakaoBuilder: KakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder()

            /*메시지 추가*/
            kakaoBuilder.addText("친구야 같이 다이어트 하자!")

            /*이미지 가로/세로 사이즈는 80px 보다 커야하며, 이미지 용량은 500kb 이하로 제한된다.*/
            var url:String  = "https://cdn.iconscout.com/public/images/icon/premium/png-512/dumbbells-weight-lifting-gym-fitness-3250aa06165832ea-512x512.png"
            kakaoBuilder.addImage(url, 160, 160)

            /*앱 실행버튼 추가*/
            kakaoBuilder.addAppButton("앱 실행 혹은 다운로드")

            /*메시지 발송*/
            kakaoLink.sendMessage(kakaoBuilder, this)

        }catch (e:Exception)
        {
            e.printStackTrace()
        }
    }
}
