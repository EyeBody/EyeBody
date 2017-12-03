package com.example.android.eyebody.exercise

import android.content.*
import android.os.Build
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import java.text.SimpleDateFormat
import java.util.*
import android.app.NotificationManager
import android.app.PendingIntent
import android.support.v4.app.NotificationCompat
import com.example.android.eyebody.R
import com.example.android.eyebody.MainActivity
import android.R.string.no
import android.R.string.yes
import android.app.Notification
import android.app.TaskStackBuilder
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.provider.Settings.Global.getString
import android.widget.Toast
import android.widget.RemoteViews
import io.vrinda.kotlinpermissions.DeviceInfo.Companion.getPackageName


/**
 * Created by ytw11 on 2017-11-06.
 */
class SMSReceiver : BroadcastReceiver() {

    private val NOTIFY_ID = 100
    private val MEAL = "com.example.android.eyebody.exercise.meal"
    private val CANCEL = "com.example.android.eyebody.exercise.cancel"
    private val BEVERAGE = "com.example.android.eyebody.exercise.beverage"
    private val DESSERT="com.example.android.eyebody.exercise.dessert"
    private var spentMoney = String()
    var menu:String?=null
    private var notificationManager:NotificationManager?=null

    override fun onReceive(context: Context, intent: Intent) {
        var now = System.currentTimeMillis()
        var date = Date(now)
        val simpleDateFormat = SimpleDateFormat("yyyy년 MM월 dd일")
        val dbHelper = DbHelper(context, "bill.db", null, 1)
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (intent.action == ACTION) {
            //Bundle 널 체크
            val bundle = intent.extras ?: return

            //pdu 객체 널 체크
            val pdusObj = bundle.get("pdus") as Array<Any> ?: return

            //message 처리
            val smsMessages = arrayOfNulls<SmsMessage>(pdusObj.size)
            for (i in pdusObj.indices) {
                if (Build.VERSION.SDK_INT >= 19) {
                    var sms: Array<SmsMessage> = Telephony.Sms.Intents.getMessagesFromIntent(intent)
                    smsMessages[i] = sms[0]
                } else smsMessages[i] = SmsMessage.createFromPdu(pdusObj[i] as ByteArray)
                //안드로이드 버전에 따라 문자를 받아오는 형식이 다르다.

                if (checkBank(smsMessages[i]?.originatingAddress)) {//카드사라는 것을 확인
                    if (wonFind(smsMessages[i]?.displayMessageBody) != "") {//얼마 썻는지 확인
                        spentMoney = wonFind(smsMessages[i]?.displayMessageBody)
                        showCustomLayoutNotification(context)
                        menu = "후식"//TODO : 노티피케이션 받아서 저장하는 걸로 하자.
                        var price = Integer.parseInt(spentMoney)
                        var time = (simpleDateFormat.format(date)).toString()
                        dbHelper.insert(time, menu!!, price)//날짜랑 메뉴 가격을 한 칼럼으로 테이블에 넣는다
                    //TODO:단순 사용 저장 보다는 노티를 날리자.//
                    }
                    else{
                    }
                }
            }
        }
    }
    private fun wonFind(price: String?): String {
        var array: List<String>? = price?.split(" ")
        for (items in array as List<String>) {
            if (items[items.length - 1] == '원') {
                var won=items.replace("원","")
                return won
            }
        }
        return ""
    }
    private fun checkBank(number: String?): Boolean {
        val numbers = arrayOf("15447200", "15881688", "15661000", "15776200", "15886700", "15888900", "15991155", "15888300", "15889955", "15884515", "15881600", "01051244019")
        //신한카드 , 국민카드, 시티카드, 현대카드, 외환카드, 삼성카드, 하나sk카드, 롯데카드, 우리카드, bc카드,농협카드
        return numbers.contains(number)
    }//문자가 오면 은행들 번호랑 비교해 가면서 은행에서 온 문자라는 것을 판별, 확인 완료

    private fun showCustomLayoutNotification(context: Context) {
        val mBuilder = createNotification(context)

        //커스텀 화면 만들기
        val remoteViews = RemoteViews(getPackageName(context), R.layout.custom_notification)
        remoteViews.setImageViewResource(R.id.img, R.mipmap.ic_launcher)
        remoteViews.setTextViewText(R.id.title, "Title")
        remoteViews.setTextViewText(R.id.message, "message")

        //노티피케이션에 커스텀 뷰 장착
        mBuilder.setContent(remoteViews)
        mBuilder.setContentIntent(createPendingIntent(context))

        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(1, mBuilder.build())
    }

    private fun createPendingIntent(context : Context): PendingIntent {
        val resultIntent = Intent(context, MainActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)

        return stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun createNotification(context:Context): NotificationCompat.Builder {
        val icon = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)
        val builder = NotificationCompat.Builder(context)
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
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
        }
        return builder
    }

    companion object {
        internal val logTag = "SmsReceiver"
        internal val ACTION = "android.provider.Telephony.SMS_RECEIVED"
    }
}
