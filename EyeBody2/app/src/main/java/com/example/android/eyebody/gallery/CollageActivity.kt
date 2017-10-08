package com.example.android.eyebody.gallery

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.android.eyebody.R
import com.kakao.kakaolink.KakaoLink
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder
import kotlinx.android.synthetic.main.activity_collage.*

class CollageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collage)

        var photoList: ArrayList<Photo> = intent.getParcelableArrayListExtra("photoList")

        //ImageSelectFragment
        var imageSelectFragment = ImageSelectFragment()
        var bundle = Bundle()
        bundle.putParcelableArrayList("photoList", photoList)
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
                shareKakao()
                //TODO 카카오톡 공유하기
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
