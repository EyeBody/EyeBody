package com.example.android.eyebody.exercise

import android.app.Activity
import android.content.*
import android.os.Build
import android.content.Intent
import android.provider.Telephony
import android.support.v4.content.ContextCompat.startActivity
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.example.android.eyebody.camera.ConfirmActivity
import java.text.SimpleDateFormat
import java.util.*
import android.content.Context.NOTIFICATION_SERVICE
import android.app.NotificationManager
import com.example.android.eyebody.R.mipmap.ic_launcher
import android.app.PendingIntent
import android.support.v4.app.NotificationCompat
import com.example.android.eyebody.R


/**
 * Created by ytw11 on 2017-11-06.
 */
class SMSReceiver : BroadcastReceiver() {
    private var spentMoney = String()

    override fun onReceive(context: Context, intent: Intent) {
        var now = System.currentTimeMillis()
        var date = Date(now)
        val simpleDateFormat = SimpleDateFormat("yyyy년 MM월 dd일")
        val dbHelper = DbHelper(context, "bill.db", null, 1)

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

                    var menu = "후식"//TODO : 노티피케이션 받아서 저장하는 걸로 하자.
                    var price = Integer.parseInt(spentMoney)
                    var time = (simpleDateFormat.format(date)).toString()
                    dbHelper.insert(time, menu, price)//날짜랑 메뉴 가격을 한 칼럼으로 테이블에 넣는다
                    //TODO:단순 사용 저장 보다는 노티를 날리자.//
                    Notification(context,"haha")
                    }
                }
            }
        }
    }

    private fun Notification(context: Context, message: String) {
        // Set Notification Title
        val strtitle = context.getString(R.string.notificationtitle)
        // Open NotificationView Class on Notification Click
        val intent = Intent(context, NotificationCustomView::class.java)
        // Send data to NotificationView Class
        intent.putExtra("title", strtitle)
        intent.putExtra("text", message)
        // Open NotificationView.java Activity
        val pIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        // Create Notification using NotificationCompat.Builder
        val builder = NotificationCompat.Builder(
                context)
                // Set Icon
                .setSmallIcon(R.mipmap.ic_launcher)
                // Set Ticker Message
                .setTicker(message)
                // Set Title
                .setContentTitle(context.getString(R.string.notificationtitle))
                // Set Text
                .setContentText(message)
                // Add an Action Button below Notification
                .addAction(R.mipmap.ic_launcher, "Action Button", pIntent)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Dismiss Notification
                .setAutoCancel(true)

        // Create Notification Manager
        val notificationmanager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build())

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
        val numbers = arrayOf("15447200", "15881688", "15661000", "15776200", "15886700", "15888900", "15991155", "15888300", "15889955", "15884515", "15881600")
        //신한카드 , 국민카드, 시티카드, 현대카드, 외환카드, 삼성카드, 하나sk카드, 롯데카드, 우리카드, bc카드,농협카드
        return numbers.contains(number)
    }//문자가 오면 은행들 번호랑 비교해 가면서 은행에서 온 문자라는 것을 판별, 확인 완료

    companion object {
        internal val logTag = "SmsReceiver"
        internal val ACTION = "android.provider.Telephony.SMS_RECEIVED"
    }
}
