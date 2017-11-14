package com.example.android.eyebody.exercise

import android.app.Notification
import android.content.*
import android.os.Build
import android.provider.Telephony
import android.support.v4.app.NotificationCompat
import android.telephony.SmsMessage
import android.util.Log
import android.widget.RemoteViews
import android.content.Context.NOTIFICATION_SERVICE
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.android.eyebody.MainActivity
import com.example.android.eyebody.R
import io.vrinda.kotlinpermissions.DeviceInfo.Companion.getPackageName


/**
 * Created by ytw11 on 2017-11-06.
 */
class SMSReceiver : BroadcastReceiver() {
    private var spentMoney = String()
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION) {
            //Bundle 널 체크
            val bundle = intent.extras ?: return

            //pdu 객체 널 체크
            val pdusObj = bundle.get("pdus") as Array<Any> ?: return

            //message 처리
            val smsMessages = arrayOfNulls<SmsMessage>(pdusObj.size)
            for (i in pdusObj.indices) {
                if (Build.VERSION.SDK_INT >= 19) {
                    var msgs: Array<SmsMessage> = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                    smsMessages[i] = msgs[0]
                } else {
                    smsMessages[i] = SmsMessage.createFromPdu(pdusObj[i] as ByteArray)
                }

                if (checkBank(smsMessages[i]?.originatingAddress)) {
                    if (wonFind(smsMessages[i]?.displayMessageBody) != "") {
                        spentMoney = wonFind(smsMessages[i]?.displayMessageBody)//TODO:어디서 사용했는지 저장
                        //spend_menu=placeFind(smsMessages[i]?.displayMessageBody)//TODO:단순 사용 저장 보다는 노티를 날리자.

                    }//얼마 썻는지 확인

                }//카드사라는 것을 확인
                /*
                Log.e(logTag, "NEW SMS " + i + "th")
                Log.e(logTag, "DisplayOriginatingAddress : " + smsMessages[i]?.displayOriginatingAddress) //문자 발신 번호
                Log.e(logTag, "DisplayMessageBody : " + smsMessages[i]?.displayMessageBody) //문자 내용
                Log.e(logTag, "OriginatingAddress : " + smsMessages[i]?.originatingAddress)//문자 발신 번호
                Log.e(logTag, "MessageBody : " + smsMessages[i]?.messageBody)
            */
            }
        }
    }

    /*private fun placeFind(place:String?):String{
        var array: List<String>? = place?.split(" ")
        for (items in array as List<String>) {
            Log.e(logTag, "haha : " + items) //문자 내용
            if (items[items.length - 1] == '원') {
                return items
            }
        }
        return ""
    }*/
    private fun wonFind(price: String?): String {
        var array: List<String>? = price?.split(" ")
        for (items in array as List<String>) {
            Log.e(logTag, "haha : " + items) //문자 내용
            if (items[items.length - 1] == '원') {
                return items
            }
        }
        return ""
    }

    private fun checkBank(number: String?): Boolean {
        val numbers = arrayOf("15447200", "15881688", "15661000", "15776200", "15886700", "15888900", "15991155", "15888300", "15889955", "15884515", "15881600")
        //신한카드 , 국민카드, 시티카드, 현대카드, 외환카드, 삼성카드, 하나sk카드, 롯데카드, 우리카드, bc카드,농협카드
        return numbers.contains(number)
    }//문자가 오면 은행들 번호랑 비교해 가면서 은행에서 온 문자라는 것을 판별, 확인 완료

    companion object {
        internal val logTag = "SmsReceiver"
        internal val ACTION = "android.provider.Telephony.SMS_RECEIVED"
    }

    fun notification() {
        val mBuilder: NotificationCompat.Builder = createNotification()

        var remoteViews: RemoteViews = RemoteViews(getPackageName(), R.layout.custom_notification)
        remoteViews.setImageViewResource(R.id.img, R.mipmap.ic_launcher)
        remoteViews.setTextViewText(R.id.title, "Title")
        remoteViews.setTextViewText(R.id.message, "message")

        mBuilder.setContent(remoteViews)
        mBuilder.setContentIntent(createPendingIntent())

        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(1, mBuilder.build())
    }

    fun createPendingIntent(): PendingIntent {
        var resultIntent: Intent =
        var stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent)

        return stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
    fun createNotification(): NotificationCompat.Builder
    {
        var icon:Bitmap = BitmapFactory . decodeResource (getResources(), R.mipmap.ic_launcher);
        var builder:NotificationCompat.Builder =  NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(icon)
                .setContentTitle("StatusBar Title")
                .setContentText("StatusBar subTitle")
                .setSmallIcon(R.mipmap.ic_launcher/*스와이프 전 아이콘*/)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        return builder
    }
}



    출처: http://leanq.tistory.com/34 [린큐의 공부한걸 기록하자]

}
