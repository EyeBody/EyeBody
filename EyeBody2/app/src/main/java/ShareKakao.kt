import android.content.Context
import com.kakao.kakaolink.KakaoLink
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder

/**
 * Created by ytw11 on 2017-11-06.
 */
class ShareKakao(var context: Context) {
    private fun shareKakao()
    {
        try{
            val kakaoLink: KakaoLink = KakaoLink.getKakaoLink(context)
            val kakaoBuilder: KakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder()

            //메시지 추가
            kakaoBuilder.addText("친구야 같이 다이어트 하자!")

            //이미지 가로/세로 사이즈는 80px 보다 커야하며, 이미지 용량은 500kb 이하로 제한된다.
            var url:String  = "https://cdn.iconscout.com/public/images/icon/premium/png-512/dumbbells-weight-lifting-gym-fitness-3250aa06165832ea-512x512.png"
            kakaoBuilder.addImage(url, 160, 160)

            //앱 실행버튼 추가
            kakaoBuilder.addAppButton("앱 실행 혹은 다운로드")

            //메시지 발송
            kakaoLink.sendMessage(kakaoBuilder, context)

        }catch (e:Exception)
        {
            e.printStackTrace()
        }
    }
}