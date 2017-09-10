package com.example.android.eyebody

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_gallery.*
import android.graphics.BitmapFactory

/*
이미지 암호화
file IO stream으로 이미지 AES 암호화/복호화
또는 파일 확장자 변환

갤러리 UI도 만들어야 함
 */
class GalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        var assetManager = getAssets()  //AssetManager 생성
        var inputStream = assetManager.open("gallery_body/front_week1.jpg");    //InputStream으로 변환
        var bitmapImg = BitmapFactory.decodeStream(inputStream);  //Bitmap으로 변환
        imageView.setImageBitmap(bitmapImg)   //Bitmap까지 변환해야 이미지뷰에 띄울 수 있음

        //cipher
        // 1. 파일 입출력 테스트 ㅠㅠ
        // 2. 바이트코드 테스트 ㅠㅠ
        // 3. cipher 클래스 사용하기 ㅠㅠ
        btn_encrypt.setOnClickListener{
            et_sample.setText("으악")
        }

        btn_decrypt.setOnClickListener{
            et_sample.setText("뜨악")
        }
    }
}
